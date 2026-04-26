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

package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.TopicType
import ru.vladsaybulin.network.models.forum.NetworkTopic
import javax.inject.Inject

interface TopicApi {
    @GET("/api/topics")
    suspend fun getTopics(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Query("forum") forumPermalink: String?,
        @Query("linked_type") linkedTypeString: String?,
        @Query("linked_id") linkedId: Long?,
        @Query("type") topicTypeString: String?,
    ): List<NetworkTopic>
}

class TopicsDataSource @Inject constructor(retrofit: Retrofit) {
    private val api: TopicApi = retrofit.create()

    suspend fun getTopics(
        page: Int? = null,
        limit: Int? = null,
        forumPermalink: String? = null,
        linkedType: TopicLinkedType? = null,
        linkedId: Long? = null,
        topicType: TopicType? = null,
    ) = api.getTopics(
        page = page,
        limit = limit,
        forumPermalink = forumPermalink,
        linkedTypeString = linkedType?.serializedValue,
        linkedId = linkedId,
        topicTypeString = topicType?.serializedValue
    )


}