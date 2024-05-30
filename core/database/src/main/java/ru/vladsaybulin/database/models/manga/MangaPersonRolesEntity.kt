package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import ru.vladsaybulin.database.models.person.PersonEntity

@Entity(
    tableName = "manga_person_roles",
    primaryKeys = ["manga_id", "person_id"],
    foreignKeys = [
        ForeignKey(
            entity = MangaDetailsEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PersonEntity::class,
            parentColumns = ["id"],
            childColumns = ["person_id"]
        )
    ]
)
data class MangaPersonRolesEntity(

    @ColumnInfo("manga_id")
    val mangaId: Long,

    @ColumnInfo("person_id")
    val personId: Long,

    @ColumnInfo("roles")
    val roles: List<String>,

    @ColumnInfo("is_main")
    val isMain: Boolean
)