package ru.vladsaybulin.feature.details.content

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.serialization.json.Json
import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagAttributes
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.domain.TextNode
import org.primeframework.transformer.service.HTMLParser
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim
import ru.vladsaybulin.core.ui.strings.AnnotatedStringBuilderContext
import ru.vladsaybulin.core.ui.strings.annotatedStringBuilderContext
import ru.vladsaybulin.core.ui.strings.link

@Composable
fun Description(
    descriptionHtml: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var textLayoutResult by remember {
        mutableStateOf<TextLayoutResult?>(null)
    }

    val isExpandable by remember {
        derivedStateOf {
            val textLayout = textLayoutResult ?: return@derivedStateOf false

            textLayout.lineCount > COLLAPSED_MAX_LINES
        }
    }

    val annotatedDescription = buildDescriptionString(html = descriptionHtml)

    val onTextClick: (Int) -> Unit = { offset ->
        val ranges = annotatedDescription.getStringAnnotations(offset, offset)
        if (ranges.isEmpty()) {
            if (isExpandable) onExpandedChange(!expanded)
        }
    }

    if (isExpandable) {
        ExpandableText(
            text = annotatedDescription,
            expanded = expanded,
            onExpandedChange = onExpandedChange,
            onTextClick = onTextClick,
            modifier = modifier
        )
    } else {
        NonExpandableText(
            text = annotatedDescription,
            onTextClick = onTextClick,
            onTextLayout = { textLayoutResult = it },
            modifier = modifier
        )
    }

}

@Composable
fun NonExpandableText(
    text: AnnotatedString,
    onTextClick: (offset: Int) -> Unit,
    onTextLayout: (TextLayoutResult) -> Unit,
    modifier: Modifier = Modifier
) {
    ClickableText(
        text = text,
        style = ShikimoriTheme.typography.bodyMedium.copy(LocalContentColor.current),
        onClick = onTextClick,
        onTextLayout = onTextLayout,
        modifier = modifier
    )
}

@Composable
fun ExpandableText(
    text: AnnotatedString,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onTextClick: (offset: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrimColor by animateColorAsState(
        targetValue = when {
            expanded -> ShikimoriTheme.colorScheme.surface.copy(alpha = 0f)
            else -> ShikimoriTheme.colorScheme.surface
        },
        label = "ForegroundGradientScrim"
    )

    val arrowDegrees by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "ArrowRotation"
    )

    Box(
        modifier = modifier.animateContentSize()
    ) {
        ClickableText(
            text = text,
            style = ShikimoriTheme.typography.bodyMedium.copy(LocalContentColor.current),
            maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_MAX_LINES,
            onClick = onTextClick,
            modifier = Modifier
                .padding(bottom = 40.dp)
                .drawForegroundGradientScrim(
                    startColor = ShikimoriTheme.colorScheme.surface.copy(alpha = 0f),
                    stopColor = scrimColor,
                    decay = 2f
                ),
        )

        IconButton(
            onClick = { onExpandedChange(!expanded) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .rotate(arrowDegrees)
        ) {
            Icon(imageVector = ShikimoriIcons.KeyboardArrowDown, contentDescription = null)
        }
    }
}

@Composable
fun buildDescriptionString(html: String): AnnotatedString {
    val context = annotatedStringBuilderContext(
        defaultTextStyle = ShikimoriTheme.typography.bodyMedium.copy(LocalContentColor.current)
    )
    val builder = AnnotatedString.Builder()

    HTMLParser().buildDocument(html, supportedHtmlTags).also {
        it.children.fastForEach {
            builder.appendNode(context, it)
        }
    }
    return builder.toAnnotatedString()
}

fun AnnotatedString.Builder.appendNode(context: AnnotatedStringBuilderContext, node: Node) {
    when (node) {
        is TagNode -> appendTagNode(context, node)
        is TextNode -> append(node.body)
    }
}

fun AnnotatedString.Builder.appendTagNode(
    context: AnnotatedStringBuilderContext,
    tagNode: TagNode
) {
    val nestedBlock: AnnotatedString.Builder.() -> Unit = { appendBody(context, tagNode) }

    when (tagNode.name) {
        "div" -> nestedBlock()
        "a" -> appendLink(context, tagNode, nestedBlock)
        "br" -> {
            append("\n")
            nestedBlock()
        }
    }
}

fun AnnotatedString.Builder.appendBody(
    context: AnnotatedStringBuilderContext,
    tagNode: TagNode
) {
    tagNode.children.fastForEach { node ->
        appendNode(context, node)
    }
}

fun AnnotatedString.Builder.appendLink(
    context: AnnotatedStringBuilderContext,
    tagNode: TagNode,
    block: AnnotatedString.Builder.() -> Unit
) {
    val dataAttrs = tagNode.dataAttrsAttribute
    val href = tagNode.hrefAttribute
    when {
        dataAttrs != null -> link(
            context = context,
            tag = "shikimori",
            annotation = dataAttrs,
            block = block
        )

        href != null -> link(
            context = context,
            tag = "url",
            annotation = href,
            block = block
        )

        else -> appendUnsupportedTag(
            tagNode = tagNode,
            block = block
        )
    }
}

fun AnnotatedString.Builder.appendUnsupportedTag(
    tagNode: TagNode,
    block: AnnotatedString.Builder.() -> Unit
) {
    tagNode.run {
        append(document.getString(begin, bodyBegin))
        block()
        if (hasClosingTag()) {
            append(document.getString(bodyEnd, end))
        }
    }
}

private val TagNode.dataAttrsAttribute
    get() = attributes["data-attrs"]

private val TagNode.hrefAttribute
    get() = attributes["href"]

private fun TagAttributes(
    doesNotRequireClosingTag: Boolean = false,
    hasPreFormattedBody: Boolean = false,
    standalone: Boolean = false,
    transformNewLines: Boolean = true
) = TagAttributes(
    doesNotRequireClosingTag,
    hasPreFormattedBody,
    standalone,
    transformNewLines
)

private val json = Json { ignoreUnknownKeys = true }

const val COLLAPSED_MAX_LINES = 7

val supportedHtmlTags = mapOf(
    "div" to TagAttributes(),
    "a" to TagAttributes(),
    "br" to TagAttributes(doesNotRequireClosingTag = true)
)