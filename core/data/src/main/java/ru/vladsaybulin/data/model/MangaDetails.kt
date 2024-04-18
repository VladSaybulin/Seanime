package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.database.models.common.IncompleteDateEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.common.ImageEntity
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.Character
import ru.vladsaybulin.model.CharacterWithRole
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.IncompleteDate
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.MangaDetails
import ru.vladsaybulin.model.Person
import ru.vladsaybulin.model.PersonWithRoles
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.Publisher
import ru.vladsaybulin.model.RelatedEntry
import ru.vladsaybulin.model.Statistic
import ru.vladsaybulin.model.asRelationType

fun MangaDetailsQuery.Manga.asExternalModel() = MangaDetails(
    id = id,
    originalName = name,
    russianName = russian,
    englishName = english,
    japaneseName = japanese,
    alternativeName = synonyms,
    licenseNameRu = licenseNameRu,
    poster = poster?.asPoster(),
    kind = kind.asMangaKind(),
    score = score?.toScore(),
    status = status.asEntryStatus(),
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asIncompleteDate(),
    releasedOn = releasedOn?.asIncompleteDate(),
    descriptionHtml = descriptionHtml?.takeIf { it.isNotBlank() },
    descriptionSource = descriptionSource?.takeIf { it.isNotBlank() },
    genres = genres?.map(MangaDetailsQuery.Genre::asGenre),
    scoreStats = scoresStats?.map(MangaDetailsQuery.ScoresStat::asScoreStat),
    userRateStatusStats = statusesStats?.map(MangaDetailsQuery.StatusesStat::asStatusStat),
    publishers = publishers.map(MangaDetailsQuery.Publisher::asPublisher),
    authors = personRoles?.map(MangaDetailsQuery.PersonRole::asPersonWithRoles),
    characters = characterRoles?.map(MangaDetailsQuery.CharacterRole::asCharacterWithRole),
    related = related?.mapNotNull(MangaDetailsQuery.Related::asRelatedEntry),
)

fun MangaDetailsQuery.Manga.asEntity() = MangaEntity(
    id = id,
    originalName = name,
    russianName = russian,
    poster = poster?.asEntity(),
    kind = kind.asMangaKind(),
    status = status.asEntryStatus(),
    score = score?.toFloat() ?: 0f,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn?.asEntity(),
    releasedOn = releasedOn?.asEntity()
)

private fun MangaDetailsQuery.Poster.asEntity() = ImageEntity(originalUrl, previewUrl)

private fun MangaDetailsQuery.AiredOn.asEntity() = IncompleteDateEntity(day, month, year)

private fun MangaDetailsQuery.ReleasedOn.asEntity() = IncompleteDateEntity(day, month, year)

private fun Double.toScore() = this.toFloat().takeIf { it != 0.0f }

private fun MangaDetailsQuery.Poster.asPoster() = Poster(
    originalUrl = originalUrl,
    previewUrl = previewUrl
)

private fun MangaDetailsQuery.AiredOn.asIncompleteDate() = IncompleteDate(day, month, year)

private fun MangaDetailsQuery.ReleasedOn.asIncompleteDate() = IncompleteDate(day, month, year)

private fun MangaDetailsQuery.Genre.asGenre() = Genre(
    id = id,
    englishName = name,
    russianName = russian,
    entryType = EntryType.Anime,
    kind = kind.asGenreKind()
)

private fun MangaDetailsQuery.ScoresStat.asScoreStat() = Statistic(score, count)

private fun MangaDetailsQuery.StatusesStat.asStatusStat() = Statistic(status.asUserRateStatus(), count)

private fun MangaDetailsQuery.Publisher.asPublisher() = Publisher(
    id = id,
    name = name
)

private fun MangaDetailsQuery.PersonRole.asPersonWithRoles() = PersonWithRoles(
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

private fun MangaDetailsQuery.CharacterRole.asCharacterWithRole() = CharacterWithRole(
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

fun MangaDetailsQuery.Related.asRelatedEntry() = if (anime != null || manga != null) {
    RelatedEntry(
        anime = anime?.run {
            Anime(
                id = id,
                name = name,
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
                releasedOn = releasedOn?.let { IncompleteDate(it.day, it.month, it.year) },
                userRate = null
            )
        },
        manga = manga?.run {
            Manga(
                id = id,
                name = name,
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
