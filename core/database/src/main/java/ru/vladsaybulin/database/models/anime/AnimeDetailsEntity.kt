package ru.vladsaybulin.database.models.anime

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.text.SeanimeTextPOJO
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus

@Entity(
    tableName = "anime_details",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["id"]
        )
    ]
)
class AnimeDetailsEntity(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("english")
    val nameEn: String?,

    @ColumnInfo("japanese")
    val nameJp: String?,

    @ColumnInfo("synonyms")
    val altNames: List<String>,

    @ColumnInfo("license_name")
    val licenseNameRu: String?,

    @ColumnInfo("rating")
    val rating: AnimeRating, //TODO TypeConverter

    @ColumnInfo("duration")
    val duration: Int,

    @ColumnInfo("next_episode_at")
    val nextEpisodeAt: Instant?,

    @Embedded("description_")
    val description: SeanimeTextPOJO?,

    @ColumnInfo("description_source")
    val descriptionSource: String?,

    @ColumnInfo("subbers")
    val subbers: List<String>?,

    @ColumnInfo("dubbers")
    val dubbers: List<String>?,

    @ColumnInfo("score_stats")
    val scoreStats: List<StatisticsItem<Int>>?,

    @ColumnInfo("status_stats")
    val statusStats: List<StatisticsItem<UserRateStatus>>?
)