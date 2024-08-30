package ru.vladsaybulin.core.domain.titledetails

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.related.RelatedEntry
import javax.inject.Inject

class GetFirstTitleRelatedStreamUseCase @Inject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>
) {
    operator fun invoke(
        titleType: EntryType,
        titleId: Long,
        limit: Int = DefaultRelatedEntriesLimit
    ): Flow<DataSlice<RelatedEntry>> = when (titleType) {
        EntryType.Anime -> animeRepository.get().getFirstAnimeRelatedStream(titleId, limit + 1)
        EntryType.Manga -> mangaRepository.get().getFirstMangaRelatedStream(titleId, limit + 1)
    }.map { relatedEntries ->
        val hasMore = relatedEntries.size == limit + 1
        DataSlice(
            data = if (hasMore) relatedEntries.slice(0..<limit) else relatedEntries,
            hasMore = hasMore
        )
    }
}

private const val DefaultRelatedEntriesLimit = 3