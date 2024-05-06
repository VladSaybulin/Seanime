package ru.vladsaybulin.model.character

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.annotatedtext.AnnotatedText
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.person.Person

data class CharacterDetails(
    val id: Long,
    val name: String,
    val nameRu: String?,
    val poster: Image?,
    val alternativeName: String?,
    val nameJp: String?,
    val description: AnnotatedText?,
    val descriptionSource: String?,
    val topicId: Long?,
    val updatedAt: Instant,
    val seyu: List<Person>,
    val animes: List<Anime>,
    val mangas: List<Manga>
)