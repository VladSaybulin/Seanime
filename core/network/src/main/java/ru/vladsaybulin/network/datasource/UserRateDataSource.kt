package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ru.vladsaybulin.common.network.ShikimoriException
import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.core.network.graphql.AnimeUserRatesQuery
import ru.vladsaybulin.core.network.graphql.MangaUserRateQuery
import ru.vladsaybulin.core.network.graphql.MangaUserRatesQuery
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.mapper.queries.asDto
import ru.vladsaybulin.network.mapper.enums.asUserRateStatusEnum
import ru.vladsaybulin.network.models.CreateUserRateDto
import ru.vladsaybulin.network.models.UserRateValuesDto
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import ru.vladsaybulin.network.models.UserRateWithEntryLinkDto
import javax.inject.Inject
import javax.inject.Singleton

private interface UserRateApi {

    @POST("/api/v2/user_rates/")
    suspend fun createUserRate(@Body userRate: JsonObject) : UserRateWithEntryLinkDto?

    @PUT("/api/v2/user_rates/{id}")
    suspend fun updateUserRate(
        @Path("id") userRateId: Long,
        @Body userRate: JsonObject
    ) : UserRateWithEntryLinkDto?

    @DELETE("/api/v2/user_rates/{id}")
    suspend fun deleteUserRate(@Path("id") userRateId: Long): Response<ResponseBody>
}

@Singleton
class UserRateDataSource @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient,
    @AuthorizedClient retrofit: Retrofit,
    private val json: Json
) {

    private val api = retrofit.create(UserRateApi::class.java)

    suspend fun getAnimeUserRates(
        page: Int,
        limit: Int,
        status: UserRateStatus,
        userId: Long? = null
    ): List<UserRateWithEntryDto> {
        val query = AnimeUserRatesQuery(
            page = page,
            limit = limit,
            status = status.asUserRateStatusEnum(),
            userId = Optional.presentIfNotNull(userId)
        )
        val response = apolloClient.query(query).execute().dataAssertNoErrors
        return response.userRates.map { it.asDto() }
    }

    suspend fun getMangaUserRates(
        page: Int,
        limit: Int,
        status: UserRateStatus,
        userId: Long? = null
    ) : List<UserRateWithEntryDto> {
        val query = MangaUserRatesQuery(
            page = page,
            limit = limit,
            status = status.asUserRateStatusEnum(),
            userId = Optional.presentIfNotNull(userId)
        )
        val response = apolloClient.query(query).execute().dataAssertNoErrors
        return response.userRates.map { it.asDto() }
    }

    suspend fun getAnimeUserRate(animeId: Long): AnimeUserRateQuery.UserRate? {
        val response = apolloClient.query(AnimeUserRateQuery(id = animeId.toString())).execute()
        val anime = response.dataAssertNoErrors.animes.firstOrNull()
            ?: throw ShikimoriException("Not found anime where id = $animeId")
        return anime.userRate
    }

    suspend fun getMangaUserRate(mangaId: Long): MangaUserRateQuery.UserRate? {
        val response = apolloClient.query(MangaUserRateQuery(id = mangaId.toString())).execute()
        val manga = response.dataAssertNoErrors.mangas.firstOrNull()
            ?: throw ShikimoriException("Not found manga where id = $mangaId")
        return manga.userRate
    }

    suspend fun createUserRate(createUserRateDto: CreateUserRateDto): UserRateWithEntryLinkDto? {
        val wrappedBody = JsonObject(
            mapOf("user_rate" to json.encodeToJsonElement(createUserRateDto))
        )
        return api.createUserRate(wrappedBody)
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