package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import ru.vladsaybulin.common.network.ShikimoriException
import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.network.di.AuthorizedClient
import javax.inject.Inject

class MangaDataSource @Inject constructor(
    @AuthorizedClient private val apolloClient: ApolloClient
) {
    suspend fun getMangaDetails(mangaId: Long): MangaDetailsQuery.Manga {
        val response = apolloClient.query(MangaDetailsQuery(id = mangaId.toString())).execute()
        return response.dataAssertNoErrors.mangas.firstOrNull()
            ?: throw ShikimoriException("Not found manga where id = $mangaId")
    }
}