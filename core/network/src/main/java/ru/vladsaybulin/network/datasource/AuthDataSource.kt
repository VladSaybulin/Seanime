package ru.vladsaybulin.network.datasource

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.POST
import ru.vladsaybulin.network.di.AuthorizedClient
import javax.inject.Inject

interface AuthApi {
    @POST("/api/users/sign_out")
    suspend fun singOut(): Response<ResponseBody>
}

class AuthDataSource @Inject constructor(
    @AuthorizedClient retrofit: Retrofit
) {
    private val api: AuthApi = retrofit.create()

    suspend fun signOut() {
        api.singOut()
    }
}