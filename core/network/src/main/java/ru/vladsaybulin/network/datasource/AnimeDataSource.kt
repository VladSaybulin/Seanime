package ru.vladsaybulin.network.datasource

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import ru.vladsaybulin.common.network.InvalidGraphqlQueryException
import ru.vladsaybulin.common.network.NotFoundException
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeDataSource @Inject constructor(private val apolloClient: ApolloClient) {
    suspend fun getAnimeDetails(id: Long): AnimeDetailsQuery.Anime {
        val response = apolloClient.query(AnimeDetailsQuery(id = id.toString())).execute()
        if (response.hasErrors()) {
            response.errors!!.forEach {
                Log.e("AnimeDataSource", it.toString())
            }
            throw InvalidGraphqlQueryException("Invalid AnimeDetailsQuery. See details in log")
        }
        return response.data!!.animes.firstOrNull()
            ?: throw NotFoundException("Not found anime where id = $id")
    }
}