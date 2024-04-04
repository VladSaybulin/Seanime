package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.CreateUserRateDto
import ru.vladsaybulin.data.model.asDbo
import ru.vladsaybulin.data.model.asDto
import ru.vladsaybulin.data.model.asUserRate
import ru.vladsaybulin.database.ShikiDatabase
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateValues
import ru.vladsaybulin.network.datasource.UserRateDataSource
import javax.inject.Inject

class UserRateRepository @Inject constructor(
    private val userRateDataSource: UserRateDataSource,
    private val database: ShikiDatabase,
    private val userRepository: UserRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    fun getAnimeUserRate(animeId: Long): Flow<UserRate?> =
        database.userRateDao.getAnimeUserRate(animeId)
            .onStart { refreshAnimeUserRate(animeId) }
            .map { it?.asUserRate() }
            .flowOn(ioDispatcher)

    fun getMangaUserRate(mangaId: Long): Flow<UserRate?> =
        database.userRateDao.getMangaUserRate(mangaId)
            .onStart { refreshMangaUserRate(mangaId) }
            .map { it?.asUserRate() }
            .flowOn(ioDispatcher)

    suspend fun refreshAnimeUserRate(animeId: Long) {
        val dbo = userRateDataSource.getAnimeUserRate(animeId)?.asDbo(animeId)
        if (dbo != null) {
            database.userRateDao.insertOrReplaceUserRate(dbo)
        }
    }

    suspend fun refreshMangaUserRate(mangaId: Long) {
        val dbo = userRateDataSource.getMangaUserRate(mangaId)?.asDbo(mangaId)
        if (dbo != null) {
            database.userRateDao.insertOrReplaceUserRate(dbo)
        }
    }

    suspend fun createUserRate(userRateValues: UserRateValues, anime: Anime) {
        createUserRate(EntryType.Anime, anime.id, userRateValues) {
            database.animeDao.insertOrReplaceAnimeEntity(anime.asDbo())
        }
    }

    suspend fun createUserRate(userRateValues: UserRateValues, manga: Manga) {
        createUserRate(EntryType.Manga, manga.id, userRateValues) {
            database.mangaDao.insertOrReplaceMangaEntity(manga.asDbo())
        }
    }

    private suspend fun createUserRate(
        entryType: EntryType,
        entryId: Long,
        userRateValues: UserRateValues,
        onSaveEntity: suspend () -> Unit
    ) {
        withContext(ioDispatcher) {
            val myId = userRepository.getMyId() ?: throw IllegalStateException("Not authorized")
            val response = try {
                userRateDataSource.createUserRate(
                    CreateUserRateDto(
                        userId = myId,
                        entryType = entryType,
                        entryId = entryId,
                        userRateValues = userRateValues
                    )
                )
            } catch (exception: Exception) {
                //TODO If UserRate exists then load and save remote UserRate
                throw exception
            }
            if (response != null) {
                onSaveEntity()
                database.userRateDao.insertOrReplaceUserRate(response.asDbo())
            }
        }
    }

    suspend fun updateUserRate(userRateId: Long, userRateValues: UserRateValues) {
        withContext(ioDispatcher) {
            val response = userRateDataSource.updateUserRate(userRateId, userRateValues.asDto())
            if (response != null) {
                database.userRateDao.insertOrReplaceUserRate(response.asDbo())
            }
        }
    }

    suspend fun deleteUserRate(userRateId: Long) {
        withContext(ioDispatcher) {
            userRateDataSource.deleteUSerRate(userRateId)
            database.userRateDao.deleteUserRate(userRateId)
        }
    }
}