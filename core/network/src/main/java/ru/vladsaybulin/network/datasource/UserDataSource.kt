package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import ru.vladsaybulin.network.models.NetworkBriefUser
import javax.inject.Inject

interface UsersApi {

    @GET("/api/users/whoami")
    suspend fun whoAmI(): NetworkBriefUser?

}

class UserDataSource @Inject constructor(
    retrofit: Retrofit
) {

    private val api = retrofit.create<UsersApi>()

    suspend fun whoAmI(): NetworkBriefUser? = api.whoAmI()
}