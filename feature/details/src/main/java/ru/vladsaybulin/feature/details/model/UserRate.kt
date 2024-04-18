package ru.vladsaybulin.feature.details.model

import ru.vladsaybulin.feature.details.DetailsUiState
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.userrate.UserRateWithEntry

fun DetailsUiState.Success.getUserRateWithEntry(): UserRateWithEntry {
    require(userRate != null)
    return UserRateWithEntry(
        userRate = userRate,
        anime = if (entryType == EntryType.Anime) {
            Anime(
                id = entryId,
                name = name,
                russianName = russianName,
                poster = poster,
                kind = animeKind ?: AnimeKind.None,
                status = status,
                score = score,
                episodes = episodes,
                episodesAired = episodesAired,
                airedOn = airedOn,
                releasedOn = releasedOn,
                userRate = userRate
            )
        } else null,
        manga = if (entryType == EntryType.Manga) {
            Manga(
                id = entryId,
                name = name,
                russianName = russianName,
                poster = poster,
                kind = mangaKind ?: MangaKind.None,
                status = status,
                score = score,
                chapters = chapters,
                volumes = volumes,
                airedOn = airedOn,
                releasedOn = releasedOn
            )
        } else null
    )
}