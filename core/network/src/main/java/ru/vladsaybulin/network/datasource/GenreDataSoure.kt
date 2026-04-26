/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.network.datasource

import com.apollographql.apollo3.ApolloClient
import ru.vladsaybulin.core.network.graphql.GenresQuery
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.network.mapper.enums.asGenreEntryTypeEnum
import ru.vladsaybulin.network.mapper.queries.asNetworkModel
import ru.vladsaybulin.network.models.common.NetworkGenre
import javax.inject.Inject

class GenreDataSource @Inject constructor(
    private val apolloClient: ApolloClient
) {
    suspend fun getGenres(entryType: EntryType): List<NetworkGenre> {
        val response = apolloClient.query(GenresQuery(entryType.asGenreEntryTypeEnum())).execute()
        return response.dataAssertNoErrors.genres.map { it.asNetworkModel(entryType) }
    }
}