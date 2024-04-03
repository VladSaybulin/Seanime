package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vladsaybulin.common.network.NotFoundException
import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.models.UserRateValuesDto
import ru.vladsaybulin.network.models.UserRateWithEntryLinkDto
import javax.inject.Inject
import javax.inject.Singleton

private interface UserRateApi {

    @PUT("/api/v2/user_rates/{id}")
    suspend fun updateUserRate(
        @Path("id") userRateId: Long,
        @Body userRate: JsonObject
    ) : UserRateWithEntryLinkDto?

    @DELETE("/api/v2/user_rates/{id}")
    suspend fun deleteUserRate(@Path("id") userRateId: Long)
}

@Singleton
class UserRateDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    @AuthorizedClient retrofit: Retrofit,
    private val json: Json
) {

    private val api = retrofit.create(UserRateApi::class.java)

    suspend fun getAnimeUserRate(animeId: Long): AnimeUserRateQuery.UserRate? {
        val response = apolloClient.query(AnimeUserRateQuery(id = animeId.toString())).execute()
        val anime = response.dataAssertNoErrors.animes.firstOrNull()
            ?: throw NotFoundException("Not found anime where id = $animeId")
        return anime.userRate
    }

    suspend fun updateUserRate(
        userRateId: Long,
        userRateValues: UserRateValuesDto
    ): UserRateWithEntryLinkDto? {
        val wrappedBody = JsonObject(
            mapOf("user_rate" to json.encodeToJsonElement(userRateValues))
        )
        return api.updateUserRate(userRateId, wrappedBody)
    }


    suspend fun deleteUSerRate(userRateId: Long) {
        api.deleteUserRate(userRateId)
    }
}