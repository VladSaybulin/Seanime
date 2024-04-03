package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.PATCH
import retrofit2.http.Path
import ru.vladsaybulin.common.network.NotFoundException
import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.models.UserRateValuesDto
import ru.vladsaybulin.network.models.UserRateWithEntryLinkDto
import javax.inject.Inject
import javax.inject.Singleton

private interface UserRateApi {

    @PATCH("/api/v2/user_rates/{id}")
    suspend fun updateUserRate(
        @Path("id") userRateId: Long,
        @Field("user_rate") userRateValuesDto: UserRateValuesDto
    ) : UserRateWithEntryLinkDto
}

@Singleton
class UserRateDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    @AuthorizedClient retrofit: Retrofit,
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
    ): UserRateWithEntryLinkDto = api.updateUserRate(userRateId, userRateValues)
}