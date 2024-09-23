package ru.vladsaybulin.feature.search

import androidx.compose.runtime.Immutable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.manga.Manga

@Immutable
data class SearchResultFlows(
    val searchAnimeResult: Flow<PagingData<Anime>>,
    val searchMangaResult: Flow<PagingData<Manga>>,
)