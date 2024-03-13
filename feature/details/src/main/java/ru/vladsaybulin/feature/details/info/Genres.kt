package ru.vladsaybulin.feature.details.info

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalContentColor
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
import ru.vladsaybulin.feature.details.model.DetailsInfo

@OptIn(ExperimentalTextApi::class)
@Composable
fun GenresLine(
    info: DetailsInfo.Genres,
    modifier: Modifier = Modifier,
    onGenreClick: (id: Long) -> Unit = {},
) {
    InfoLine(
        icon = { InfoIconPlaceholder() },
        modifier = modifier
    ) {
        val genresText = buildAnnotatedString {
            append(stringResource(id = info.headerStringId))
            append(": ")
            val start = length
            info.genres.forEach {
                if (this.length > start) {
                    append(", ")
                }
                withAnnotation(
                    tag = GenreTag,
                    annotation = it.id.toString()
                ) {
                    withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                        append(it.russianName)
                    }
                }
            }
        }

        ClickableText(
            text = genresText,
            style = LocalTextStyle.current.copy(color = LocalContentColor.current)
        ) { index ->
            genresText.getStringAnnotations(
                tag = GenreTag,
                start = index,
                end = index
            ).firstOrNull()?.let {
                onGenreClick(it.item.toLong())
            }
        }
    }
}

private const val GenreTag = "genre"