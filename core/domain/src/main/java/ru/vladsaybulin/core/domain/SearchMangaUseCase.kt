package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.manga.mangaKind
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class SearchMangaUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
    operator fun invoke(queryMap: Map<QueryMapKey, String>) = mangaRepository.getPagedManga(
        queryMap = if (!queryMap.containsKey(QueryMapKey.Kind)) {
            queryMap + (QueryMapKey.Kind to serializedMangaKind)
        } else queryMap
    )

    companion object {
        val serializedMangaKind
            get() = mangaKind.joinToString(separator = ",") { it.serializedName }
    }
}