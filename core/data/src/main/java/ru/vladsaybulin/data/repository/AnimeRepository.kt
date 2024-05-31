package ru.vladsaybulin.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.animeAuthorEntities
import ru.vladsaybulin.data.model.animeCharacterEntities
import ru.vladsaybulin.data.model.animeRelatedEntities
import ru.vladsaybulin.data.model.animeScreenshotEntities
import ru.vladsaybulin.data.model.animeStudioCrossRefs
import ru.vladsaybulin.data.model.animeVideoEntities
import ru.vladsaybulin.data.model.asAnimeDetailsEntity
import ru.vladsaybulin.data.model.asAnimeEntity
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.characterEntityShells
import ru.vladsaybulin.data.model.genreEntityShells
import ru.vladsaybulin.data.model.genresCrossReferences
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.relatedAnimeEntityShell
import ru.vladsaybulin.data.model.relatedMangaEntityShell
import ru.vladsaybulin.data.model.studioEntityShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.AnimeDetailsDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.PopulatedAnimeAuthor
import ru.vladsaybulin.database.models.anime.PopulatedAnimeCharacter
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.lastrequest.LastRequestEntity
import ru.vladsaybulin.database.models.lastrequest.LastRequestType
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.datasource.UserRateDataSource
import ru.vladsaybulin.network.models.NetworkAnime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val userRateDataSource: UserRateDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val ongoingAnimeDao: OngoingAnimeDao,
    private val animeDetailsDao: AnimeDetailsDao,
    private val personDao: PersonDao,
    private val characterDao: CharacterDao,
    private val mangaDao: MangaDao,
    private val genreDao: GenreDao,
    private val lastRequestDao: LastRequestDao,
    private val authRepository: AuthRepository,
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {

    fun getPagedAnime(
        queryMap: Map<QueryMapKey, String>,
        pagingConfig: PagingConfig = DefaultSearchPagingConfig
    ): Flow<PagingData<AnimeWithUserRate>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { getPagedAnimePagingSource(queryMap) }
    )
        .flow
        .flowOn(ioDispatcher)

    fun getOngoingAnime(limit: Int = 10): Flow<List<Anime>> =
        flowOf { ongoingAnimeDao.getOngoingAnime(limit) }
            .onStart { loadOngoingAnime(INITIAL_PAGE, limit, true) }
            .map { it.map(AnimeEntity::asExternalModel) }
            .flowOn(ioDispatcher)

    fun getAnimeDetails(animeId: Long): Flow<AnimeDetails> =
        animeDetailsDao.getAnimeDetails(animeId)
            .map { it.asExternalModel() }
            .flowOn(ioDispatcher)

    fun getMainAnimeAuthors(animeId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getMainAnimeAuthors(animeId)
            .map { it.map(PopulatedAnimeAuthor::asExternalModel) }
            .flowOn(ioDispatcher)

    fun getMainAnimeCharacters(animeId: Long): Flow<List<CharacterWithRole>> =
        animeDetailsDao.getMainAnimeCharacters(animeId)
            .map { it.map(PopulatedAnimeCharacter::asExternalModel) }
            .flowOn(ioDispatcher)

    fun getSimilarAnimes(animeId: Long): Flow<List<Anime>> =
        flowOf {
            animeDataSource.getSimilarAnimes(animeId).map { it.asExternalModel() }
        }.flowOn(ioDispatcher)

    fun getAllAnimeAuthors(animeId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getAllAnimeAuthors(animeId)
            .map { it.map(PopulatedAnimeAuthor::asExternalModel) }

    suspend fun syncAnimeDetails(animeId: Long) {
        withContext(ioDispatcher) {
            sync(
                ttl = DefaultAnimeTTL,
                readLastUpdateDate = {
                    lastRequestDao.getLastRequestDate(LastRequestType.ANIME, animeId)
                },
                refresh = { refreshAnimeDetails(animeId) }
            )
        }
    }

    suspend fun refreshAnimeDetails(animeId: Long) {
        withContext(ioDispatcher) {
            val animeDetails = animeDataSource.getAnimeDetails(animeId)

            databaseTransactionRunner {
                animeDetailsDao.insertOrReplaceStudios(animeDetails.studioEntityShells())
                animeDetails.genreEntityShells()?.let {
                    genreDao.insertOrReplaceGenres(it)
                }
                animeDetails.characterEntityShells()?.let {
                    characterDao.insertOrReplaceCharacters(it)
                }
                animeDetails.personEntityShells()?.let {
                    personDao.insertOrReplacePersons(it)
                }
                animeDetails.relatedAnimeEntityShell()?.let {
                    animeDao.insertOrReplaceAnimes(it)
                }
                animeDetails.relatedMangaEntityShell()?.let {
                    mangaDao.insertOrReplaceMangas(it)
                }

                animeDao.upsertAnime(animeDetails.asAnimeEntity())
                animeDetailsDao.insertOrReplaceAnimeDetails(animeDetails.asAnimeDetailsEntity())

                animeDetails.animeStudioCrossRefs().let {
                    animeDetailsDao.insertAnimeStudioCrossReferences(it)
                }
                animeDetails.genresCrossReferences()?.let {
                    animeDetailsDao.insertAnimeGenreCrossReferences(it)
                }
                animeDetails.animeCharacterEntities()?.let {
                    animeDetailsDao.insertAnimeCharacters(it)
                }
                animeDetails.animeAuthorEntities()?.let {
                    animeDetailsDao.insertAnimeAuthors(it)
                }
                animeDetails.animeRelatedEntities()?.let {
                    animeDetailsDao.insertAnimeRelated(it)
                }
                animeDetails.animeScreenshotEntities().let {
                    animeDetailsDao.insertAnimeScreenshots(it)
                }
                animeDetails.animeVideoEntities()?.let {
                    animeDetailsDao.insertAnimeVideos(it)
                }

                lastRequestDao.insertOrReplaceLastRequestDate(
                    LastRequestEntity(
                        requestType = LastRequestType.ANIME,
                        targetId = animeId,
                        requestDate = Clock.System.now()
                    )
                )
            }
        }
    }

    private suspend fun loadOngoingAnime(
        pageNumber: Int,
        pageSize: Int,
        isRefreshing: Boolean
    ): Boolean {
        val response = animeDataSource.getAnime(
            page = pageNumber,
            limit = pageSize,
            statusString = EntryStatus.Ongoing.serializedName,
            order = Order.Popularity,
            myListString = "!watching,!rewatching"
        )
        val animes = response.map(NetworkAnime::asEntity)
        val ongoingAnime = animes.mapIndexed { index, dbo ->
            OngoingAnimeEntity(
                animeId = dbo.id,
                order = pageNumber * pageSize + index
            )
        }

        if (isRefreshing) {
            ongoingAnimeDao.deleteAll()
        }
        animeDao.insertOrReplaceAnimes(animes)
        ongoingAnimeDao.insertAll(ongoingAnime)

        return animes.size < pageSize
    }

    private fun getPagedAnimePagingSource(queryMap: Map<QueryMapKey, String>) =
        object : AbstractShikimoriPagingSource<AnimeWithUserRate>() {
            override suspend fun loadPage(
                pageNumber: Int,
                pageSize: Int
            ): LoadResult<Int, AnimeWithUserRate> = try {
                val networkAnimes = animeDataSource.getAnime(
                    page = pageNumber,
                    limit = pageSize,
                    queryMap = queryMap
                )
                val animeEntities = networkAnimes.map { it.asEntity() }
                val userRatesEntities = networkAnimes.mapNotNull { it.userRateEntityShell() }

                if (animeEntities.isNotEmpty()) {
                    animeDao.insertOrReplaceAnimes(animeEntities)
                }

                if (userRatesEntities.isNotEmpty()) {
                    userRateDao.insertOrReplaceUserRates(userRatesEntities)
                }

                val animes = networkAnimes.map {
                    AnimeWithUserRate(
                        anime = it.asExternalModel(),
                        userRate = it.userRate?.asExternalModel()
                    )
                }

                LoadResult.Page(
                    data = animes,
                    nextKey = if (animes.size == pageSize) pageNumber + 1 else null,
                    prevKey = null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }
}

private val DefaultAnimeTTL = 1.days

private const val INITIAL_PAGE = 1