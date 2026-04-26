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
import com.apollographql.apollo3.api.Optional.Companion.presentIfNotNull
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.common.network.ShikimoriException
import ru.vladsaybulin.core.network.graphql.MangaDetailsQuery
import ru.vladsaybulin.core.network.graphql.MangaQuery
import ru.vladsaybulin.core.network.graphql.MangaRolesQuery
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.mapper.queries.asNetworkModel
import ru.vladsaybulin.network.models.manga.NetworkManga
import ru.vladsaybulin.network.models.common.NetworkTitleRoles
import ru.vladsaybulin.network.models.manga.NetworkMangaDetails
import ru.vladsaybulin.network.util.getOrderEnum
import javax.inject.Inject

interface MangaApi {
    @GET("/api/mangas/{id}/similar")
    suspend fun getSimilarManga(@Path("id") mangaId: Long): List<NetworkManga>
}

class MangaDataSource @Inject constructor(
    private val apolloClient: ApolloClient,
    retrofit: Retrofit
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

    suspend fun getMangaDetails(mangaId: Long): NetworkMangaDetails {
        val response = apolloClient.query(MangaDetailsQuery(id = mangaId.toString())).execute()
        return response.dataAssertNoErrors.mangas.singleOrNull()?.asNetworkModel()
            ?: throw ShikimoriException("Not found manga where id = $mangaId")
    }

    suspend fun getMangaRoles(animeId: Long): NetworkTitleRoles {
        val response = apolloClient.query(MangaRolesQuery(id = animeId.toString())).execute()
        return checkNotNull(response.dataAssertNoErrors.mangas.singleOrNull()) {
            "Not found manga with id = $animeId"
        }.asNetworkModel()
    }

    suspend fun getSimilarManga(mangaId: Long): List<NetworkManga> = api.getSimilarManga(mangaId)
}