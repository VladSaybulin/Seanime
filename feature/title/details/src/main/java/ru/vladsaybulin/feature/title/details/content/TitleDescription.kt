package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.text.SeanimeExpandableText
import ru.vladsaybulin.core.ui.text.onSeanimeTextLinkClickAdapter
import ru.vladsaybulin.model.annotatedtext.SeanimeText

@Composable
fun TitleDescription(
    description: SeanimeText,
    onAnimeClick: (Long) -> Unit?,
    onMangaClick: (Long) -> Unit?,
    onCharacterClick: (Long) -> Unit?,
    onPersonClick: (Long) -> Unit?,
    onUrlClick: (String) -> Unit?,
) {
    SeanimeExpandableText(
        text = description,
        style = SeanimeTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp),
        onLinkClick = onSeanimeTextLinkClickAdapter(
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick,
            onCharacterClick = onCharacterClick,
            onPersonClick = onPersonClick,
            onUrlClick = onUrlClick,
        )
    )
}