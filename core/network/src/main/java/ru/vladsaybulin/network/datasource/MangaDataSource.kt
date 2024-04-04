package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.common.network.ShikimoriException
import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.network.di.AuthorizedClient
import ru.vladsaybulin.network.models.MangaDto
import javax.inject.Inject

interface MangaApi {
    @GET("/api/mangas/{id}/similar")
    suspend fun getSimilarManga(@Path("id") mangaId: Long): List<MangaDto>
}

class MangaDataSource @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient,
    @AuthorizedClient private val retrofit: Retrofit
) {

    private val api: MangaApi = retrofit.create()

    suspend fun getMangaDetails(mangaId: Long): MangaDetailsQuery.Manga {
        val response = apolloClient.query(MangaDetailsQuery(id = mangaId.toString())).execute()
        return response.dataAssertNoErrors.mangas.firstOrNull()
            ?: throw ShikimoriException("Not found manga where id = $mangaId")
    }

    suspend fun getSimilarManga(mangaId: Long): List<MangaDto> = api.getSimilarManga(mangaId)
}