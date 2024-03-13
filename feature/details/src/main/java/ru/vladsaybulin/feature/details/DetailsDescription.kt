package ru.vladsaybulin.feature.details

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagAttributes
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.domain.TextNode
import org.primeframework.transformer.service.BBCodeParser
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim
import ru.vladsaybulin.feature.details.model.DetailsDescription

@Composable
fun DetailsDescriptionContent(
    description: DetailsDescription,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
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
            text = buildDescriptionString(code = description.code),
            style = ShikimoriTheme.typography.bodyMedium.copy(LocalContentColor.current),
            maxLines = if (expanded) Int.MAX_VALUE else COLLAPSED_MAX_LINES,
            modifier = Modifier
                .padding(bottom = 40.dp)
                .drawForegroundGradientScrim(
                    startColor = ShikimoriTheme.colorScheme.surface.copy(alpha = 0f),
                    targetColor = scrimColor,
                    decay = 2f
                )
        ) {
            onExpandedChange(!expanded)
        }

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
fun buildDescriptionString(code: String): AnnotatedString {
    val doc = remember(code.hashCode()) {
        BBCodeParser().buildDocument(code, tagAttributes)
    }

    return buildAnnotatedString {
        doc.children.forEach { node ->
            processNode(node, colorScheme = ShikimoriTheme.colorScheme)
        }
    }
}

fun AnnotatedString.Builder.processNode(node: Node, colorScheme: ColorScheme) {
    when (node) {
        is TextNode -> processTextNode(node)
        is TagNode -> processTagNode(node, colorScheme)
    }
}

fun AnnotatedString.Builder.processTextNode(textNode: TextNode) {
    append(textNode.body)
}

@OptIn(ExperimentalTextApi::class)
fun AnnotatedString.Builder.processTagNode(
    tagNode: TagNode,
    colorScheme: ColorScheme
) {
    val nested: AnnotatedString.Builder.() -> Unit = {
        tagNode.children.forEach {
            processNode(it, colorScheme)
        }
    }

    when (tagNode.name) {
        "anime",
        "manga",
        "ranobe",
        "character",
        "person",
        "url" ->
            withAnnotation(
                tag = tagNode.name,
                annotation = checkNotNull(tagNode.attribute)
            ) {
                withStyle(
                    style = SpanStyle(
                        color = colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    ),
                    block = nested
                )
            }
        else -> nested()
    }
}

private val tagAttributes = mapOf(
    "anime" to TagAttributes(false, false, false, true),
    "manga" to TagAttributes(false, false, false, true),
    "ranobe" to TagAttributes(false, false, false, true),
    "character" to TagAttributes(false, false, false, true),
    "person" to TagAttributes(false, false, false, true)
)

const val COLLAPSED_MAX_LINES = 7

//May be parse html. Regex expression to find type and id in data-attrs attribute: "id":"(\d+)"|"type":"(.+)"