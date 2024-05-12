package ru.vladsaybulin.database.models.userrate

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.userrate.UserRateWithEntry

data class PopulatedUserRate(

    @Embedded
    val userRateEntity: UserRateEntity,

    @Relation(
        entity = AnimeEntity::class,
        parentColumn = "anime_id",
        entityColumn = "id"
    )
    val animeDbo: AnimeEntity?,

    @Relation(
        entity = MangaEntity::class,
        parentColumn = "manga_id",
        entityColumn = "id"
    )
    val mangaDbo: MangaEntity?,
)

fun PopulatedUserRate.asExternalModel() = UserRateWithEntry(
    anime = animeDbo?.asExternalModel(),
    manga = mangaDbo?.asExternalModel(),
    userRate = userRateEntity.asExternalModel()
)