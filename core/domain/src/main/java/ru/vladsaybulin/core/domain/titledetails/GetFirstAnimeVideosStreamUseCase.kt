package ru.vladsaybulin.core.domain.titledetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.common.DataSlice
import javax.inject.Inject

class GetFirstAnimeVideosStreamUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    operator fun invoke(animeId: Long, limit: Int = DefaultAnimeVideosLimit): Flow<DataSlice<Video>> =
        animeRepository.getFirstAnimeVideos(animeId, limit + 1)
            .map { videos ->
                val hasMore = videos.size == limit + 1
                DataSlice(
                    data = if (hasMore) videos.slice(0..<limit) else videos,
                    hasMore = hasMore
                )
            }
}

private const val DefaultAnimeVideosLimit = 5