package ru.vladsaybulin.network.models

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import ru.vladsaybulin.model.topic.TopicEvent
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.TopicType
import ru.vladsaybulin.network.util.serializers.TopicEventSerializer
import ru.vladsaybulin.network.util.serializers.TopicLinkedTypeSerializer
import ru.vladsaybulin.network.util.serializers.TopicTypeSerializer

@Serializable
data class TopicDto(
    @SerialName("id") val id: Long,
    @SerialName("topic_title") val title: String,
    @SerialName("body") val body: String?,
    @SerialName("html_body") val bodyHtml: String?,
    @SerialName("html_footer") val footerHtml: String?,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("forum") val forum: ForumDto,
    @SerialName("user") val user: NetworkBriefUser,
    @Serializable(TopicTypeSerializer::class)
    @SerialName("type")
    val type: TopicType,
    @Serializable(TopicLinkedTypeSerializer::class)
    @SerialName("linked_type")
    val linkedType: TopicLinkedType,
    //This linked field is a JsonObject because it can be any model specified in the linkedType
    @SerialName("linked") val linked: JsonObject?,
    @SerialName("viewed") val viewed: Boolean,
    @SerialName("last_comment_viewed") val lastCommentViewed: Boolean,
    @Serializable(TopicEventSerializer::class)
    @SerialName("event")
    val event: TopicEvent,
    @SerialName("episode") val episode: Int?
)

fun TopicDto.decodeLinkedAnime(json: Json): NetworkAnime {
    require(linkedType == TopicLinkedType.Anime) {
        "Linked is not Anime"
    }
    requireNotNull(linked) { "Linked is null" }
    return json.decodeFromJsonElement(linked)
}

fun TopicDto.decodeLinkedManga(json: Json): NetworkManga {
    require(linkedType == TopicLinkedType.Manga || linkedType == TopicLinkedType.Ranobe) {
        "Linked is not Manga"
    }
    requireNotNull(linked) { "Linked is null" }
    return json.decodeFromJsonElement(linked)
}