package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.api.Optional.Companion.presentIfNotNull
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.common.network.ShikimoriException
import ru.vladsaybulin.core.network.graphql.AnimeDetailsQuery
import ru.vladsaybulin.core.network.graphql.AnimeQuery
import ru.vladsaybulin.core.network.graphql.AnimeRolesQuery
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.mapper.enums.asOrderEnum
import ru.vladsaybulin.network.mapper.queries.asNetworkModel
import ru.vladsaybulin.network.mapper.queries.asNetworkModels
import ru.vladsaybulin.network.models.NetworkAnime
import ru.vladsaybulin.network.models.anime.NetworkAnimeDetails
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import ru.vladsaybulin.network.util.getOrderEnum
import javax.inject.Inject
import javax.inject.Singleton

interface AnimeApi {
    @GET("/api/animes/{anime_id}/similar/")
    suspend fun getSimilarAnime(@Path("anime_id") animeId: Long): List<NetworkAnime>
}

@Singleton
class AnimeDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    retrofit: Retrofit
) {
    private val api: AnimeApi = retrofit.create()

    suspend fun getAnime(
        page: Int? = null,
        limit: Int? = null,
        queryMap: Map<QueryMapKey, String>,
    ): List<NetworkAnime>  {
        val response = apolloClient.query(
            AnimeQuery(
                page = presentIfNotNull(page),
                limit = presentIfNotNull(limit),
                order = presentIfNotNull(queryMap.getOrderEnum()),
                kind = presentIfNotNull(queryMap[QueryMapKey.Kind]),
                status = presentIfNotNull(queryMap[QueryMapKey.Status]),
                season = presentIfNotNull(queryMap[QueryMapKey.Season]),
                score = presentIfNotNull(queryMap[QueryMapKey.Score]?.toInt()),
                duration = presentIfNotNull(queryMap[QueryMapKey.Duration]),
                rating = presentIfNotNull(queryMap[QueryMapKey.Rating]),
                genre = presentIfNotNull(queryMap[QueryMapKey.Genre]),
                studio = presentIfNotNull(queryMap[QueryMapKey.Studio]),
                franchise = presentIfNotNull(queryMap[QueryMapKey.Franchise]),
                censored = presentIfNotNull(queryMap[QueryMapKey.Censored]?.toBooleanStrict()),
                mylist = presentIfNotNull(queryMap[QueryMapKey.MyList]),
                ids = presentIfNotNull(queryMap[QueryMapKey.Ids]),
                excludeIds = presentIfNotNull(queryMap[QueryMapKey.ExcludedIds]),
                search = presentIfNotNull(queryMap[QueryMapKey.Search])
            )
        ).execute()
        return response.dataAssertNoErrors.animes.map { it.asNetworkModels() }
    }

    suspend fun getAnimeDetails(animeId: Long): NetworkAnimeDetails {
        val response = apolloClient.query(AnimeDetailsQuery(id = animeId.toString())).execute()
        return response.dataAssertNoErrors.animes.firstOrNull()?.asNetworkModel()
            ?: throw ShikimoriException("Not found anime where id = $animeId")
    }

    suspend fun getAnimeRoles(animeId: Long): NetworkTitleRoles {
        val response = apolloClient.query(AnimeRolesQuery(id = animeId.toString())).execute()
        return checkNotNull(response.dataAssertNoErrors.animes.singleOrNull()) {
            "Not found anime with id = $animeId"
        }.asNetworkModel()
    }

    suspend fun getSimilarAnimes(animeId: Long) = api.getSimilarAnime(animeId)
}

