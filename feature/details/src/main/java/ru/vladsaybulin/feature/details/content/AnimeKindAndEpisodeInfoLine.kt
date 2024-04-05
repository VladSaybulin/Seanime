package ru.vladsaybulin.feature.details.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.strings.animeKindString
import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryType

internal fun DetailsUiState.Success.showAnimeKindAndEpisode(): Boolean {
    if (entryType != EntryType.Anime) return false

    return (animeKind != null && animeKind != AnimeKind.None) ||
            (episodes > 0 || episodesAired > 0) ||
            (episodeDuration != null && episodeDuration > 0)

}


