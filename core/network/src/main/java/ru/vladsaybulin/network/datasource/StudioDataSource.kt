package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import ru.vladsaybulin.network.models.anime.NetworkStudio
import javax.inject.Inject

interface StudioApi {

    @GET("/api/studios")
    suspend fun getStudios(): List<NetworkStudio>
}

class StudioDataSource @Inject constructor(retrofit: Retrofit) {
    private val api: StudioApi = retrofit.create()

    suspend fun getStudios(): List<NetworkStudio> = api.getStudios()
}