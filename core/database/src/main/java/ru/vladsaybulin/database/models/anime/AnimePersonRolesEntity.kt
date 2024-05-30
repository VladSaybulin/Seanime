package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.person.PersonEntity

@Entity(
    tableName = "anime_person_roles",
    primaryKeys = ["anime_id", "person_id"],
    foreignKeys = [
        ForeignKey(
            entity = AnimeDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["person_id"]
        )
    ]
)
data class AnimePersonRolesEntity(

    @ColumnInfo("anime_id")
    val animeId: Long,

    @ColumnInfo("person_id")
    val personId: Long,

    @ColumnInfo("roles_en")
    val roles: List<String>,

    @ColumnInfo("is_main")
    val isMain: Boolean
)