package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.genre.asExternalModel
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.network.datasource.GenreDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

class GenreRepository @Inject constructor(
    private val genreDataSource: GenreDataSource,
    private val genreDao: GenreDao,
    private val shikiPreferencesDataSource: ShikiPreferencesDataSource,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getGenres(entryType: EntryType, genreKind: GenreKind): List<Genre> =
        withContext(ioDispatcher) {
            syncGenres(entryType)
            genreDao.getGenresByKind(entryType, genreKind)
                .map(GenreEntity::asExternalModel)
        }


    private suspend fun syncGenres(entryType: EntryType) {
        sync(
            ttl = GENRES_TTL,
            lastRequestDateFlow = when (entryType) {
                EntryType.Anime -> shikiPreferencesDataSource.animeGenresLastRequestDate
                EntryType.Manga -> shikiPreferencesDataSource.mangaGenresLastRequestDate
            },
            updateLastRequest = when (entryType) {
                EntryType.Anime -> shikiPreferencesDataSource::setLastAnimeGenresRequestDate
                EntryType.Manga -> shikiPreferencesDataSource::setLastMangaGenresRequestDate
            }
        ) {
            val response = genreDataSource.getGenres(entryType)

            genreDao.deleteGenresByEntryType(entryType)
            genreDao.insertAllGenres(response.map { it.asEntity() })
        }
    }
}

private val GENRES_TTL = 10.days