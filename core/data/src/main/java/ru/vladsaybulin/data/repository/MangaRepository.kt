package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asSimilarEntry
import ru.vladsaybulin.model.MangaDetails
import ru.vladsaybulin.model.SimilarEntry
import ru.vladsaybulin.network.datasource.MangaDataSource
import ru.vladsaybulin.network.models.MangaDto
import javax.inject.Inject

class MangaRepository @Inject constructor(
    private val mangaDataSource: MangaDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun geMangaDetails(mangaId: Long): Flow<MangaDetails> = flow {
        emit(mangaDataSource.getMangaDetails(mangaId).asExternalModel())
    }.flowOn(ioDispatcher)

    fun getSimilarMangas(mangaId: Long): Flow<List<SimilarEntry>> = flow {
        emit(mangaDataSource.getSimilarManga(mangaId).map(MangaDto::asSimilarEntry))
    }.flowOn(ioDispatcher)
}