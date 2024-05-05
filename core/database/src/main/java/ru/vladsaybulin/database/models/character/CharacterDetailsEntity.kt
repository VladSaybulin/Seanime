package ru.vladsaybulin.database.models.character

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.common.ProtoTextAnnotation

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

    @ColumnInfo("description")
    val descriptionText: String?,

    @ColumnInfo("description_annotations")
    val descriptionAnnotations: List<ProtoTextAnnotation>,

    @ColumnInfo("description_source")
    val descriptionSource: String?,

    @ColumnInfo("topic_id")
    val topicId: Long?,

    @ColumnInfo("updated_at")
    val updatedAt: Instant,



    )