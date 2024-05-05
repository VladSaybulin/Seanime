package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.common.AnnotatedTextPOJO

@Entity(
    tableName = "character_details",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        )
    ]
)
data class CharacterDetailsEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("name_jp")
    val nameJp: String?,

    @ColumnInfo("alt_names")
    val altNames: String?,

    @Embedded("description_")
    val description: AnnotatedTextPOJO,

    @ColumnInfo("description_source")
    val descriptionSource: String?,

    @ColumnInfo("topic_id")
    val topicId: Long?,

    @ColumnInfo("updated_at")
    val updatedAt: Instant,



    )