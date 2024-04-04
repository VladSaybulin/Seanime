package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.common.network.NotFoundException
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.models.AnimeDto
import javax.inject.Inject
import javax.inject.Singleton

interface AnimeApi {
    @GET("/api/animes/{anime_id}/similar/")
    suspend fun getSimilarAnime(@Path("anime_id") animeId: Long): List<AnimeDto>
}

@Singleton
class AnimeDataSource @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient,
    retrofit: Retrofit
) {
    private val api: AnimeApi = retrofit.create()

    suspend fun getAnimeDetails(animeId: Long): AnimeDetailsQuery.Anime {
        val response = apolloClient.query(AnimeDetailsQuery(id = animeId.toString())).execute()
        return response.dataAssertNoErrors.animes.firstOrNull()
            ?: throw NotFoundException("Not found anime where id = $animeId")
    }

    suspend fun getSimilarAnimes(animeId: Long) = api.getSimilarAnime(animeId)
}