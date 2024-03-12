package ru.vladsaybulin.feature.details.info

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withStyle
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.feature.details.model.DetailsInfo

@OptIn(ExperimentalTextApi::class)
@Composable
internal fun StudiosLine(
    info: DetailsInfo.Studios,
    modifier: Modifier = Modifier,
    onStudioClick: (id: Long) -> Unit = {}
) {
    InfoLine(
        icon = { InfoIcon(imageVector = ShikimoriIcons.Groups) },
        modifier = modifier
    ) {
        val annotatedString = buildAnnotatedString {
            append(
                when (info.studios.size) {
                    1 -> stringResource(id = R.string.single_studio)
                    else -> stringResource(id = R.string.studios)
                }
            )
            append(": ")
            val initialLength = length
            info.studios.forEach {
                if (this.length > initialLength) {
                    append(", ")
                }
                withAnnotation(
                    tag = StudioTag,
                    annotation = it.id.toString()
                ) {
                    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(it.name)
                    }
                }
            }
        }

        ClickableText(
            text = annotatedString,
            onClick = { index ->
                annotatedString.getStringAnnotations(
                    tag = StudioTag,
                    start = index,
                    end = index
                ).firstOrNull()?.let {
                    onStudioClick(it.item.toLong())
                }
            },
            style = LocalTextStyle.current
        )
    }
}

private const val StudioTag = "studio"