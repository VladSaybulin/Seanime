package ru.vladsaybulin.data.util

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import java.io.IOException

private const val INITIAL_PAGE = 1

val DefaultSearchPagingConfig = PagingConfig(
    pageSize = 50,
    prefetchDistance = 10,
    initialLoadSize = 50,
    enablePlaceholders = false
)

abstract class AbstractShikimoriPagingSource<T : Any> : PagingSource<Int, T>() {

    abstract suspend fun loadPage(
        pageNumber: Int,
        pageSize: Int
    ): LoadResult<Int, T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val pageNumber = params.key ?: INITIAL_PAGE
        val pageSize = params.loadSize

        return loadPage(pageNumber, pageSize)
    }
}

@OptIn(ExperimentalPagingApi::class)
abstract class AbstractShikimoriRemoteMediator<T : Any> : RemoteMediator<Int, T>() {

    abstract suspend fun loadPage(
        pageNumber: Int,
        pageSize: Int,
        loadType: LoadType
    ): MediatorResult

    final override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, T>
    ): MediatorResult {
        val pageNumber = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)

            LoadType.APPEND -> state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1)
            }
        }
        return try {
            loadPage(
                pageNumber = pageNumber ?: INITIAL_PAGE,
                pageSize = state.config.pageSize,
                loadType = loadType
            )
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }
}