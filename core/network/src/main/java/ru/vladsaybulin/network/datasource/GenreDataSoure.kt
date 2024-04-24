package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import ru.vladsaybulin.core.network.graphql.GenresQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.network.mapper.enums.asGenreEntryTypeEnum
import ru.vladsaybulin.network.mapper.queries.asNetworkModel
import ru.vladsaybulin.network.models.NetworkGenre
import javax.inject.Inject

class GenreDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getGenres(entryType: EntryType): List<NetworkGenre> {
        val response = apolloClient.query(GenresQuery(entryType.asGenreEntryTypeEnum())).execute()
        return response.dataAssertNoErrors.genres.map { it.asNetworkModel(entryType) }
    }
}