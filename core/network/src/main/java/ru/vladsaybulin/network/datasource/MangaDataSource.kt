package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional.Companion.presentIfNotNull
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.common.network.ShikimoriException
import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.core.network.graphql.MangaQuery
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.mapper.queries.asNetworkModel
import ru.vladsaybulin.network.models.NetworkManga
import ru.vladsaybulin.network.util.getOrderEnum
import javax.inject.Inject

interface MangaApi {
    @GET("/api/mangas/{id}/similar")
    suspend fun getSimilarManga(@Path("id") mangaId: Long): List<NetworkManga>
}

class MangaDataSource @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient,
    @AuthorizedClient private val retrofit: Retrofit
) {

    private val api: MangaApi = retrofit.create()

    suspend fun getManga(
        page: Int? = null,
        limit: Int? = null,
        queryMap: Map<QueryMapKey, String>
    ): List<NetworkManga> {
        val response = apolloClient.query(
            MangaQuery(
                page = presentIfNotNull(page),
                limit = presentIfNotNull(limit),
                order = presentIfNotNull(queryMap.getOrderEnum()),
                kind = presentIfNotNull(queryMap[QueryMapKey.Kind]),
                status = presentIfNotNull(queryMap[QueryMapKey.Status]),
                score = presentIfNotNull(queryMap[QueryMapKey.Score]?.toInt()),
                genre = presentIfNotNull(queryMap[QueryMapKey.Genre]),
                publisher = presentIfNotNull(queryMap[QueryMapKey.Publisher]),
                franchise = presentIfNotNull(queryMap[QueryMapKey.Franchise]),
                censored = presentIfNotNull(queryMap[QueryMapKey.Censored]?.toBooleanStrict()),
                mylist = presentIfNotNull(queryMap[QueryMapKey.MyList]),
                ids = presentIfNotNull(queryMap[QueryMapKey.Ids]),
                excludeIds = presentIfNotNull(queryMap[QueryMapKey.ExcludedIds]),
                search = presentIfNotNull(queryMap[QueryMapKey.Search])
            )
        ).execute()
        return response.dataAssertNoErrors.mangas.map { it.asNetworkModel() }
    }

    suspend fun getMangaDetails(mangaId: Long): MangaDetailsQuery.Manga {
        val response = apolloClient.query(MangaDetailsQuery(id = mangaId.toString())).execute()
        return response.dataAssertNoErrors.mangas.firstOrNull()
            ?: throw ShikimoriException("Not found manga where id = $mangaId")
    }

    suspend fun getSimilarManga(mangaId: Long): List<NetworkManga> = api.getSimilarManga(mangaId)
}