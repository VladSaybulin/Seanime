package ru.vladsaybulin.core.domain.search

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.mangaKind
import ru.vladsaybulin.model.manga.ranobeKind
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class GetPagedMangaSearchUseCase @Inject constructor(
    private val mangaRepository: MangaRepository,
    private val shikimoriAuthorization: ShikimoriAuthorization
) {
    operator fun invoke(
        queryMap: Map<QueryMapKey, String>,
        isRanobe: Boolean,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ): Flow<PagingData<Manga>> {
        val finalQueryMap = queryMap.let { original ->
            if (!original.containsKey(QueryMapKey.Kind))  {
                val kindValue = (if (isRanobe) ranobeKind else mangaKind)
                    .joinToString(separator = ",", transform = MangaKind::serializedName)
                original + (QueryMapKey.Kind to kindValue)
            } else original
        }
        return pagedSearch(authStateFlow = shikimoriAuthorization.shikimoriAuthState, config = pagingConfig) {
            mangaRepository.mangaSearchPagingSource(finalQueryMap)
        }
    }
}