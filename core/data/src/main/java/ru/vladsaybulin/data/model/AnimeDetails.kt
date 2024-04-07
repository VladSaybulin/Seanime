package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.database.models.AnimeDbo
import ru.vladsaybulin.database.models.IncompleteDateDbo
import ru.vladsaybulin.database.models.PosterDbo
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.Character
import ru.vladsaybulin.model.CharacterWithRole
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.IncompleteDate
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.Person
import ru.vladsaybulin.model.PersonWithRoles
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.RelatedEntry
import ru.vladsaybulin.model.Screenshot
import ru.vladsaybulin.model.Statistic
import ru.vladsaybulin.model.Studio
import ru.vladsaybulin.model.Video
import ru.vladsaybulin.model.asRelationType
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.AiredOn as NetworkAiredOn
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.CharacterRole as NetworkCharacterRole
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.Genre as NetworkGenre
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.PersonRole as NetworkPersonRole
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.Poster as NetworkPoster
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.Related as NetworkRelated
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.ReleasedOn as NetworkReleasedOn
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.ScoresStat as NetworkScoreStat
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.Screenshot as NetworkScreenshot
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.StatusesStat as NetworkStatusStat
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.Studio as NetworkStudio
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery.Video as NetworkVideo

fun AnimeDetailsQuery.Anime.asExternalModel() = AnimeDetails(
    id = id,
    originalName = name,
    russianName = russian,
    englishName = english,
    japaneseName = japanese,
    alternativeName = synonyms,
    licenseNameRu = licenseNameRu,
    poster = poster?.asPoster(),
    kind = kind.asAnimeKind(),
    score = score?.toScore(),
    status = status.asEntryStatus(),
    rating = rating.asAnimeRating(),
    episodes = episodes,
    episodesAired = episodesAired,
    nextEpisodeAt = nextEpisodeAt,
    duration = duration,
    airedOn = airedOn?.asIncompleteDate(),
    releasedOn = releasedOn?.asIncompleteDate(),
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map(NetworkGenre::asGenre),
    subbers = fansubbers,
    dubbers = fandubbers,
    scoreStats = scoresStats?.map(NetworkScoreStat::asScoreStat),
    userRateStatusStats = statusesStats?.map(NetworkStatusStat::asStatusStat),
    studios = studios.map(NetworkStudio::asStudio),
    authors = personRoles?.map(NetworkPersonRole::asPersonWithRoles),
    characters = characterRoles?.map(NetworkCharacterRole::asCharacterWithRole),
    related = related?.mapNotNull(NetworkRelated::asRelatedEntry),
    screenshots = screenshots.map(NetworkScreenshot::asScreenshot),
    videos = videos.map(NetworkVideo::asVideo)
)

fun AnimeDetailsQuery.Anime.asDbo() = AnimeDbo(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asDbo(),
    kind = kind.asAnimeKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn?.asDbo(),
    releasedOn = releasedOn?.asDbo()
)

private fun AnimeDetailsQuery.Poster.asDbo() = PosterDbo(originalUrl, previewUrl)

private fun AnimeDetailsQuery.AiredOn.asDbo() = IncompleteDateDbo(day, month, year)

private fun AnimeDetailsQuery.ReleasedOn.asDbo() = IncompleteDateDbo(day, month, year)

private fun Double.toScore() = this.toFloat().takeIf { it != 0.0f }

private fun NetworkPoster.asPoster() = Poster(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

private fun NetworkAiredOn.asIncompleteDate() = IncompleteDate(day, month, year)

private fun NetworkReleasedOn.asIncompleteDate() = IncompleteDate(day, month, year)

private fun NetworkGenre.asGenre() = Genre(
    id = id,
    englishName = name,
    russianName = russian,
    entryType = EntryType.Anime,
    kind = kind.asGenreKind()
)

private fun NetworkScoreStat.asScoreStat() = Statistic(score, count)

private fun NetworkStatusStat.asStatusStat() = Statistic(status.asUserRateStatus(), count)

private fun NetworkStudio.asStudio() = Studio(
    id = id,
    name = name,
    imageUrl = imageUrl
)

private fun NetworkPersonRole.asPersonWithRoles() = PersonWithRoles(
    person = Person(
        id = person.id,
        originalName = person.name,
        russianName = person.russian,
        poster = person.poster?.let {
            Poster(originalUrl = it.originalUrl, previewUrl = it.previewUrl)
        }
    ),
    englishRoles = rolesEn,
    russianRoles = rolesRu
)

private fun NetworkCharacterRole.asCharacterWithRole() = CharacterWithRole(
    character = Character(
        id = character.id,
        originalName = character.name,
        russianName = character.russian,
        poster = character.poster?.let {
            Poster(originalUrl = it.originalUrl, previewUrl = it.previewUrl)
        }
    ),
    isMain = rolesEn.contains("Main")
)

fun NetworkRelated.asRelatedEntry() = if (anime != null || manga != null) {
    RelatedEntry(
        anime = anime?.run {
            Anime(
                id = id,
                originalName = name,
                russianName = russian,
                poster = poster?.let { p ->
                    Poster(
                        previewUrl = p.previewUrl,
                        originalUrl = p.originalUrl
                    )
                },
                kind = kind.asAnimeKind(),
                status = status.asEntryStatus(),
                score = score?.toScore(),
                episodes = episodes,
                episodesAired = episodesAired,
                airedOn = airedOn?.let { IncompleteDate(it.day, it.month, it.year) },
                releasedOn = releasedOn?.let { IncompleteDate(it.day, it.month, it.year) }
            )
        },
        manga = manga?.run {
            Manga(
                id = id,
                originalName = name,
                russianName = russian,
                poster = poster?.let { p ->
                    Poster(
                        previewUrl = p.previewUrl,
                        originalUrl = p.originalUrl
                    )
                },
                kind = kind.asMangaKind(),
                status = status.asEntryStatus(),
                score = score?.toScore(),
                chapters = chapters,
                volumes = volumes,
                airedOn = airedOn?.let { IncompleteDate(it.day, it.month, it.year) },
                releasedOn = releasedOn?.let { IncompleteDate(it.day, it.month, it.year) }
            )
        },
        relationType = relationEn.asRelationType()
    )
} else null

fun NetworkScreenshot.asScreenshot() = Screenshot(
    x166Url = x166Url,
    x332Url = x332Url,
    originalUrl = originalUrl
)

fun NetworkVideo.asVideo() = Video(
    name = name,
    previewImageUrl = "https:$imageUrl",
    videoUrl = url,
    playerUrl = "https:$imageUrl",
    kind = kind.asVideoKind()
)