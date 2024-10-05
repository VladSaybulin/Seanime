package ru.vladsaybulin.core.domain.search

import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import ru.vladsaybulin.model.auth.ShikimoriAuthState

internal fun <T : Any> pagedSearch(
    authStateFlow: StateFlow<ShikimoriAuthState>,
    config: PagingConfig = DefaultSearchPagingConfig,
    pagingSourceFactory: () -> PagingSource<Int, T>
) = flow {
    coroutineScope {
        val invalidatingPagingSourceFactory = InvalidatingPagingSourceFactory(pagingSourceFactory)

        launch {
            authStateFlow
                .drop(1) // Drop initial state
                .filter { it == ShikimoriAuthState.LOGGED_IN }
                .collect { invalidatingPagingSourceFactory.invalidate() }
        }

        emitAll(
            Pager(
                config = config,
                pagingSourceFactory = invalidatingPagingSourceFactory
            ).flow
        )
    }
}

private const val PAGE_SIZE = 50
private const val PREFETCH_DISTANCE = 20
private const val INITIAL_LOAD_SIZE = 50

internal val DefaultSearchPagingConfig = PagingConfig(
    pageSize = PAGE_SIZE,
    prefetchDistance = PREFETCH_DISTANCE,
    initialLoadSize = INITIAL_LOAD_SIZE
)