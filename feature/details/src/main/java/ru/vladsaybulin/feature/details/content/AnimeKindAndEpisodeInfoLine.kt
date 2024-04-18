package ru.vladsaybulin.feature.details.content

import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryType

internal fun DetailsUiState.Success.showAnimeKindAndEpisode(): Boolean {
    if (entryType != EntryType.Anime) return false

    return (animeKind != null && animeKind != AnimeKind.None) ||
            (episodes > 0 || episodesAired > 0) ||
            (episodeDuration != null && episodeDuration > 0)

}


