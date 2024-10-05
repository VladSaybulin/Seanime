package ru.vladsaybulin.data.repository

import androidx.paging.PagingState

internal fun getShikimoriRefreshKey(state: PagingState<Int, *>): Int? =
    state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }

internal const val ShikimoriFirstPage = 1