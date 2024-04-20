package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.manga.ranobeKind
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class SearchRanobeUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
    operator fun invoke(queryMap: Map<QueryMapKey, String>) = mangaRepository.getPagedManga(
        queryMap = if (!queryMap.containsKey(QueryMapKey.Kind)) {
            queryMap + (QueryMapKey.Kind to serializedRanobeKind)
        } else queryMap
    )

    companion object {
        val serializedRanobeKind
            get() = ranobeKind.joinToString(separator = ",") { it.serializedName }
    }
}