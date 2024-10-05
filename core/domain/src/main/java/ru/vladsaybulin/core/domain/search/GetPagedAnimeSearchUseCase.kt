package ru.vladsaybulin.core.domain.search

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class GetPagedAnimeSearchUseCase @Inject internal constructor(
    private val animeRepository: AnimeRepository,
    private val shikimoriAuthorization: ShikimoriAuthorization
) {
    operator fun invoke(
        queryMap: Map<QueryMapKey, String>,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ): Flow<PagingData<Anime>> = pagedSearch(
        authStateFlow = shikimoriAuthorization.shikimoriAuthState,
        config = pagingConfig
    ) {
        animeRepository.animeSearchPagingSource(queryMap)
    }
}

