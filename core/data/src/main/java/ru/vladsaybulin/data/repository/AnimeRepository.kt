package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.data.model.asAnime
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.models.AnimeDto
import javax.inject.Inject

class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    @Dispatcher(ShikiDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getAnimeDetails(animeId: Long): Flow<AnimeDetails> = flow {
        emit(animeDataSource.getAnimeDetails(animeId).asExternalModel())
    }.flowOn(ioDispatcher)

    fun getSimilarAnimes(animeId: Long): Flow<List<Anime>> = flow {
        emit(animeDataSource.getSimilarAnimes(animeId).map(AnimeDto::asAnime))
    }.flowOn(ioDispatcher)
}