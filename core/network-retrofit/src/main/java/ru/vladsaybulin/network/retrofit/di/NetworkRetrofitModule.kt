package ru.vladsaybulin.network.retrofit.di


import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.vladsaybulin.network.common.di.AuthorizedClient

private const val BASE_URL = "https://shikimori.one/"

@Module
@InstallIn(SingletonComponent::class)
class NetworkRetrofitModule {

    @Provides
    fun provideJson(): Json = Json

    @Provides
    @AuthorizedClient
    fun provideAuthorizedRetrofit(
        @AuthorizedClient okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideUnauthorizedRetrofit(
        okHttpClient: OkHttpClient,
        json: Json,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(json.asConverterFactory(MediaType.get("application/json")))
        .client(okHttpClient)
        .build()


}
