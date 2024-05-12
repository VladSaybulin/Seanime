package ru.vladsaybulin.database.models.manga

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.database.models.text.SeanimeTextPOJO
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus

@Entity(tableName = "manga_details")
data class MangaDetailsEntity(

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

    @Embedded("description_")
    val description: SeanimeTextPOJO?,

    @ColumnInfo("description_source")
    val descriptionSource: String?,

    @ColumnInfo("score_stats")
    val scoreStats: List<StatisticsItem<Int>>?,

    @ColumnInfo("status_stats")
    val statusStats: List<StatisticsItem<UserRateStatus>>?
)