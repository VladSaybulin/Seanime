package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.network.models.user.NetworkBriefUser
import javax.inject.Inject

interface UsersApi {

    @GET("/api/users/whoami")
    suspend fun whoAmI(): NetworkBriefUser?

    @GET("/api/users/{id}/info")
    suspend fun getUserBriefById(@Path("id") id: Long): NetworkBriefUser

}

class UserDataSource @Inject constructor(
    retrofit: Retrofit
) {

    private val api = retrofit.create<UsersApi>()

    suspend fun whoAmI(): NetworkBriefUser? = api.whoAmI()

    suspend fun getUserBriefById(id: Long): NetworkBriefUser = api.getUserBriefById(id)
}