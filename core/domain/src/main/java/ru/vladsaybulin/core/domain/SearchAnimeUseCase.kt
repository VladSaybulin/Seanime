package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.model.search.QueryMapKey
import javax.inject.Inject

class SearchAnimeUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    operator fun invoke(queryMap: Map<QueryMapKey, String>) =
        animeRepository.getPagedAnime(queryMap)
}