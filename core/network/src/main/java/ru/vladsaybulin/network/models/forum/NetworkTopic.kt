/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.network.models.forum

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import ru.vladsaybulin.model.topic.TopicEvent
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.TopicType
import ru.vladsaybulin.network.models.user.NetworkBriefUser
import ru.vladsaybulin.network.util.serializers.TopicEventSerializer
import ru.vladsaybulin.network.util.serializers.TopicLinkedTypeSerializer
import ru.vladsaybulin.network.util.serializers.TopicTypeSerializer

@Serializable
data class NetworkTopic(
    @SerialName("id") val id: Long,
    @SerialName("topic_title") val title: String,
    @SerialName("body") val body: String?,
    @SerialName("html_body") val bodyHtml: String?,
    @SerialName("html_footer") val footerHtml: String?,
    @SerialName("created_at") val createdAt: Instant,
    @SerialName("comments_count") val commentsCount: Int,
    @SerialName("forum") val forum: NetworkForum,
    @SerialName("user") val user: NetworkBriefUser,
    @Serializable(TopicTypeSerializer::class)
    @SerialName("type")
    val type: TopicType,
    @Serializable(TopicLinkedTypeSerializer::class)
    @SerialName("linked_type")
    val linkedType: TopicLinkedType,
    @SerialName("linked_id") val linkedId: Long?,
    //This linked field is a JsonObject because it can be any model specified in the linkedType
    @SerialName("linked") val linked: JsonObject?,
    @SerialName("viewed") val viewed: Boolean?,
    @SerialName("last_comment_viewed") val lastCommentViewed: Boolean?,
    @Serializable(TopicEventSerializer::class)
    @SerialName("event")
    val event: TopicEvent,
    @SerialName("episode") val episode: Int?
)

