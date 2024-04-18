package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.TopicType
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.models.TopicDto
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
    ): List<TopicDto>
}

class TopicsDataSource @Inject constructor(
    @AuthorizedClient retrofit: Retrofit
) {
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