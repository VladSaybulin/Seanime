package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.anime.AnimePersonRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPersonRolesEntity
import ru.vladsaybulin.network.models.person.NetworkPersonWithRoles

fun NetworkPersonWithRoles.asAnimePersonWithRoles(animeId: Long) = AnimePersonRolesEntity(
    personId = person.id,
    roles = roles,
    isMain = roles.isMainPersonRoles(),
    animeId = animeId
)

fun NetworkPersonWithRoles.asMangaPersonWithRoles(mangaId: Long) = MangaPersonRolesEntity(
    personId = person.id,
    roles = roles,
    isMain = roles.isMainPersonRoles(),
    mangaId = mangaId
)

fun List<String>.isMainPersonRoles() = any { it in MainRoles }

private val MainRoles = listOf(
    "Director",
    "Original Creator",
    "Story",
    "Art",
    "Story & Art"
)