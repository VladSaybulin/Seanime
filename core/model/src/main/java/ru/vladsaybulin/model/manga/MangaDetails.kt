package ru.vladsaybulin.model.manga

import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.common.Poster
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.common.Statistic
import ru.vladsaybulin.model.userrate.UserRateStatus

data class MangaDetails(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val englishName: String?,
    val japaneseName: String?,
    val alternativeName: List<String>,
    val licenseNameRu: String?,
    val poster: Poster?,
    val kind: MangaKind,
    val score: Float?,
    val status: EntryStatus,
    val chapters: Int,
    val volumes: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?,
    val descriptionHtml: String?,
    val descriptionSource: String?,
    val genres: List<Genre>?,
    val scoreStats: List<Statistic<Int>>?,
    val userRateStatusStats: List<Statistic<UserRateStatus>>?,
    val publishers: List<Publisher>,
    val authors: List<PersonWithRoles>?,
    val characters: List<CharacterWithRole>?,
    val related: List<RelatedEntry>?
)