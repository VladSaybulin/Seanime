package ru.vladsaybulin.feature.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.model.AnimeWithUserRate
import ru.vladsaybulin.model.manga.MangaWithUserRate

sealed class SearchResultState {

    data object None : SearchResultState()

    data class Animes(val pagingDataFlow: Flow<PagingData<AnimeWithUserRate>>) : SearchResultState()

    data class Mangas(val pagingDataFlow: Flow<PagingData<MangaWithUserRate>>) : SearchResultState()
}