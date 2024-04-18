package ru.vladsaybulin.database.models.topic

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.model.topic.TopicEvent
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.TopicType

@Entity(
    tableName = "topics",
    foreignKeys = [
        ForeignKey(
            entity = AnimeEntity::class,
            parentColumns = ["id"],
            childColumns = ["anime_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = MangaEntity::class,
            parentColumns = ["id"],
            childColumns = ["manga_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class TopicDbo(
    @ColumnInfo("id")
    @PrimaryKey
    val id: Long,
    @ColumnInfo("topic_title") val title: String,
    @ColumnInfo("body") val body: String?,
    @ColumnInfo("html_body") val bodyHtml: String?,
    @ColumnInfo("html_footer") val footerHtml: String?,
    @ColumnInfo("created_at") val createdAt: Instant,
    @ColumnInfo("comments_count") val commentsCount: Int,
    @ColumnInfo("forum") val forumPermalink: String,
    @ColumnInfo("user_id") val userId: Long,
    @ColumnInfo("type") val type: TopicType,
    @ColumnInfo("linked_type") val linkedType: TopicLinkedType,
    @ColumnInfo("anime_id") val animeId: Long?,
    @ColumnInfo("manga_id") val mangaId: Long?,
    @ColumnInfo("viewed") val viewed: Boolean,
    @ColumnInfo("last_comment_viewed") val lastCommentViewed: Boolean,
    @ColumnInfo("event") val event: TopicEvent,
    @ColumnInfo("episode") val episode: Int?
)
