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
import ru.vladsaybulin.core.network.graphql.type.UserRateOrderInputType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.mapper.enums.asSortOrderEnum
import ru.vladsaybulin.network.mapper.enums.asUserRateOrderFieldEnum
import ru.vladsaybulin.network.mapper.enums.asUserRateStatusEnum
import ru.vladsaybulin.network.mapper.queries.asNetworkModel
import ru.vladsaybulin.network.mapper.queries.asNetworkModels
import ru.vladsaybulin.network.models.CreateUserRateDto
import ru.vladsaybulin.network.models.NetworkUserRate
import ru.vladsaybulin.network.models.NetworkUserRateValues
import ru.vladsaybulin.network.models.UserRateWithEntryDto
import ru.vladsaybulin.network.models.UserRateWithEntryLinkDto
import javax.inject.Inject
import javax.inject.Singleton

private interface UserRateApi {

    @POST("/api/v2/user_rates/")
    suspend fun createUserRate(@Body userRate: JsonObject): UserRateWithEntryLinkDto?

    @PUT("/api/v2/user_rates/{id}")
    suspend fun updateUserRate(
        @Path("id") userRateId: Long,
        @Body userRate: JsonObject
    ): UserRateWithEntryLinkDto?

    @DELETE("/api/v2/user_rates/{id}")
    suspend fun deleteUserRate(@Path("id") userRateId: Long): Response<ResponseBody>
}

@Singleton
class UserRateDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    retrofit: Retrofit,
    private val json: Json
) {

    private val api = retrofit.create(UserRateApi::class.java)

    suspend fun getAnimeUserRates(
        page: Int,
        limit: Int,
        status: UserRateStatus,
        field: UserRateOrderField,
        sortOrder: UserRateOrder,
        userId: Long? = null
    ): List<UserRateWithEntryDto> {
        val query = AnimeUserRatesQuery(
            page = page,
            limit = limit,
            status = status.asUserRateStatusEnum(),
            userId = Optional.presentIfNotNull(userId),
            orderInput = UserRateOrderInputType(
                field = field.asUserRateOrderFieldEnum(),
                order = sortOrder.asSortOrderEnum()
            )
        )
        val response = apolloClient.query(query).execute().dataAssertNoErrors
        return response.userRates.map { it.asNetworkModel() }
    }

    suspend fun getMangaUserRates(
        page: Int,
        limit: Int,
        status: UserRateStatus,
        field: UserRateOrderField,
        sortOrder: UserRateOrder,
        userId: Long? = null
    ): List<UserRateWithEntryDto> {
        val query = MangaUserRatesQuery(
            page = page,
            limit = limit,
            status = status.asUserRateStatusEnum(),
            userId = Optional.presentIfNotNull(userId),
            orderInput = UserRateOrderInputType(
                field = field.asUserRateOrderFieldEnum(),
                order = sortOrder.asSortOrderEnum()
            )
        )
        val response = apolloClient.query(query).execute().dataAssertNoErrors
        return response.userRates.map { it.asNetworkModels() }
    }

    suspend fun getAnimeUserRate(animeId: Long): NetworkUserRate? {
        val response = apolloClient.query(AnimeUserRateQuery(ids = animeId.toString(), limit = 1))
            .execute()
        val anime = response.dataAssertNoErrors.animes.singleOrNull()
            ?: throw ShikimoriException("Not found anime where id = $animeId")
        return anime.userRate?.asNetworkModel()
    }

    suspend fun getMangaUserRate(mangaId: Long): NetworkUserRate? {
        val response = apolloClient.query(MangaUserRateQuery(ids = mangaId.toString(), limit = 1))
            .execute()
        val manga = response.dataAssertNoErrors.mangas.singleOrNull()
            ?: throw ShikimoriException("Not found manga where id = $mangaId")
        return manga.userRate?.asNetworkModel()
    }

    suspend fun getAnimeUserRatesByAnimeIds(animeIds: List<Long>): Map<Long, NetworkUserRate?> =
        apolloClient.query(
            AnimeUserRateQuery(
                ids = animeIds.joinToString(separator = ","),
                limit = animeIds.size
            )
        ).execute()
            .dataAssertNoErrors.animes
            .associate { it.id to it.userRate!!.asNetworkModel() }

    suspend fun getMangaUserRatesByMangaIds(mangaIds: List<Long>): Map<Long, NetworkUserRate?> =
        apolloClient.query(
            MangaUserRateQuery(ids = mangaIds.joinToString(separator = ","), limit = mangaIds.size)
        ).execute()
            .dataAssertNoErrors.mangas
            .associate { it.id to it.userRate?.asNetworkModel() }

    suspend fun createUserRate(createUserRateDto: CreateUserRateDto): UserRateWithEntryLinkDto? {
        val wrappedBody = JsonObject(
            mapOf("user_rate" to json.encodeToJsonElement(createUserRateDto))
        )
        return api.createUserRate(wrappedBody)
    }

    suspend fun updateUserRate(
        userRateId: Long,
        userRateValues: NetworkUserRateValues
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