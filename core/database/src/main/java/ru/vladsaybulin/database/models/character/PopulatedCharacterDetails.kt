package ru.vladsaybulin.database.models.character

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.person.asExternalModel
import ru.vladsaybulin.model.character.CharacterDetails

data class PopulatedCharacterDetails(

    @Embedded
    val characterDetailsEntity: CharacterDetailsEntity,

    @Relation(
        entity = CharacterEntity::class,
        parentColumn = "id",
        entityColumn = "id"
    )
    val characterEntity: CharacterEntity,

    @Relation(
        parentColumn = "id",
        entity = PersonEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterSeyuCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "seyu_id"
        )
    )
    val seyuEntities: List<PersonEntity>,

    @Relation(
        parentColumn = "id",
        entity = AnimeEntity::class,
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterAnimeCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "anime_id"
        )
    )
    val animeEntities: List<AnimeEntity>,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CharacterMangaCrossRef::class,
            parentColumn = "character_id",
            entityColumn = "manga_id"
        )
    )
    val mangaEntities: List<MangaEntity>,
)

fun PopulatedCharacterDetails.asExternalModel() = CharacterDetails(
    id = characterEntity.id,
    name = characterEntity.name,
    nameRu = characterEntity.nameRu,
    poster = characterEntity.image?.asExternalModel(),
    alternativeName = characterDetailsEntity.altNames,
    nameJp = characterDetailsEntity.nameJp,
    description = characterDetailsEntity.description.asExternalModel(),
    descriptionSource = characterDetailsEntity.descriptionSource,
    topicId = characterDetailsEntity.topicId,
    updatedAt = characterDetailsEntity.updatedAt,
    seyu = seyuEntities.map { it.asExternalModel() },
    animes = animeEntities.map { it.asExternalModel() },
    mangas = mangaEntities.map { it.asExternalModel() }
)