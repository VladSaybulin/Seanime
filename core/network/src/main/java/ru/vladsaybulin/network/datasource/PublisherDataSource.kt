package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import ru.vladsaybulin.network.models.manga.NetworkPublisher
import javax.inject.Inject

interface PublisherApi {

    @GET("/api/publishers")
    suspend fun getPublishers(): List<NetworkPublisher>
}

class PublisherDataSource @Inject constructor(retrofit: Retrofit) {

    private val api: PublisherApi = retrofit.create()

    suspend fun getPublishers(): List<NetworkPublisher> = api.getPublishers()

}