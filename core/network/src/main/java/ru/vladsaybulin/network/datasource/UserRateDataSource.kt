package ru.vladsaybulin.network.datasource

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import ru.vladsaybulin.common.network.InvalidGraphqlQueryException
import ru.vladsaybulin.common.network.NotFoundException
import ru.vladsaybulin.core.network.graphql.AnimeUserRateQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRateDataSource @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun getAnimeUserRate(animeId: Long): AnimeUserRateQuery.UserRate? {
        val response = apolloClient.query(AnimeUserRateQuery(id = animeId.toString())).execute()
        if (response.hasErrors()) {
            response.errors!!.forEach {
                Log.e("AnimeDataSource", it.toString())
            }
            throw InvalidGraphqlQueryException("Invalid AnimeDetailsQuery. See details in log")
        }
        val anime = response.data!!.animes.firstOrNull()
            ?: throw NotFoundException("Not found anime where id = $animeId")
        return anime.userRate
    }

}