package ru.vladsaybulin.feature.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.manga.MangaWithUserRate

@Immutable
data class SearchResultFlows(
    val searchAnimeResult: Flow<PagingData<AnimeWithUserRate>>,
    val searchMangaResult: Flow<PagingData<MangaWithUserRate>>,
)