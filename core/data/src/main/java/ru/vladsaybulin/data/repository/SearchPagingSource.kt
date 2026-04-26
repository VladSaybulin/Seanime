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

package ru.vladsaybulin.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState

internal class SearchPagingSource<T : Any>(
    private val load: suspend (page: Int, limit: Int) -> List<T>
) : PagingSource<Int, T>() {
    override fun getRefreshKey(state: PagingState<Int, T>): Int? = getShikimoriRefreshKey(state)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> = try {
        val page = params.key ?: ShikimoriFirstPage
        val limit = params.loadSize
        load(page, limit).let { data ->
            LoadResult.Page(
                itemsBefore = (page - 1) * limit,
                data = data,
                prevKey = if (page == ShikimoriFirstPage) null else page - 1,
                nextKey = if (data.size < limit) null else page + 1
            )
        }
    } catch (exception: Exception) {
        LoadResult.Error(exception)
    }
}