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