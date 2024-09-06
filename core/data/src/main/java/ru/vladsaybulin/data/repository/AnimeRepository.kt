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
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.animeCharacterEntities
import ru.vladsaybulin.data.model.animePersonRolesEntities
import ru.vladsaybulin.data.model.relatedAnimeEntityShells
import ru.vladsaybulin.data.model.animeScreenshotEntityShells
import ru.vladsaybulin.data.model.animeStudioCrossRefs
import ru.vladsaybulin.data.model.animeVideoEntityShells
import ru.vladsaybulin.data.model.asAnimeDetailsEntity
import ru.vladsaybulin.data.model.asAnimeEntity
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.characterEntityShells
import ru.vladsaybulin.data.model.genreEntityShells
import ru.vladsaybulin.data.model.animeGenresCrossReferences
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.animeRelatedEntities
import ru.vladsaybulin.data.model.relatedMangaEntityShells
import ru.vladsaybulin.data.model.studioEntityShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
import ru.vladsaybulin.data.util.DefaultSearchPagingConfig
import ru.vladsaybulin.data.util.flowOf
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.AnimeDetailsDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeSimilarAnimeCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.PopulatedAnimeAuthor
import ru.vladsaybulin.database.models.anime.PopulatedAnimeCharacter
import ru.vladsaybulin.database.models.anime.PopulatedAnimeRelated
import ru.vladsaybulin.database.models.anime.PopulatedSimilarAnime
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.anime.AnimeWithUserRate
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedEntry
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.models.NetworkAnime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(
    private val animeDataSource: AnimeDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val ongoingAnimeDao: OngoingAnimeDao,
    private val animeDetailsDao: AnimeDetailsDao,
    private val personDao: PersonDao,
    private val characterDao: CharacterDao,
    private val mangaDao: MangaDao,
    private val genreDao: GenreDao,
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

    fun getAnimeDetailsStream(animeId: Long): Flow<AnimeDetails> =
        animeDetailsDao.getAnimeDetails(animeId).map { it.asExternalModel() }

    fun getAnimeMainCharactersStream(animeId: Long): Flow<List<Character>> =
        animeDetailsDao.getMainAnimeCharacters(animeId).map { mainCharacters ->
            mainCharacters.map { it.asExternalModel().character }
        }

    fun getAnimeMainAuthorsStream(animeId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getMainAnimeAuthors(animeId).map { it.map(PopulatedAnimeAuthor::asExternalModel) }

    fun getFirstAnimeRelatedStream(animeId: Long, limit: Int): Flow<List<RelatedEntry>> =
        animeDetailsDao.getFirstAnimeRelated(animeId, limit).map { it.map(PopulatedAnimeRelated::asExternalModel) }

    fun getAnimeScreenshots(animeId: Long): Flow<List<Image>> =
        animeDetailsDao.getAnimeScreenshots(animeId).map { it.map(AnimeScreenshotEntity::asExternalModel) }

    fun getFirstAnimeVideos(animeId: Long, limit: Int): Flow<List<Video>> =
        animeDetailsDao.getFirstAnimeVideos(animeId, limit).map { it.map(AnimeVideoEntity::asExternalModel) }

    fun getSimilarAnimes(animeId: Long): Flow<List<Anime>> =
        animeDetailsDao.getSimilarAnimes(animeId).map { it.map(PopulatedSimilarAnime::asExternalModel) }

    fun getAllAnimeAuthors(animeId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getAllAnimeAuthors(animeId)
            .map { it.map(PopulatedAnimeAuthor::asExternalModel) }

    fun getAllAnimeRelatedTitles(animeId: Long): Flow<List<RelatedEntry>> =
        animeDetailsDao.getAllAnimeRelatedTitles(animeId)
            .map { it.map(PopulatedAnimeRelated::asExternalModel) }

    fun getAllAnimeCharacters(animeId: Long): Flow<List<CharacterWithRole>> =
        animeDetailsDao.getAllAnimeCharacters(animeId)
            .map { it.map(PopulatedAnimeCharacter::asExternalModel) }

    fun getAllAnimeVideos(animeId: Long): Flow<List<Video>> =
        animeDetailsDao.getAllAnimeVideos(animeId)
            .map { it.map(AnimeVideoEntity::asExternalModel) }

    suspend fun refreshAnimeDetails(animeId: Long) {
        withContext(ioDispatcher) {
            val response = animeDataSource.getAnimeDetails(animeId)

            val animeEntity = response.asAnimeEntity()
            val animeDetailsEntity = response.asAnimeDetailsEntity()

            val genresEntities = response.genreEntityShells()
            val studiosEntities = response.studioEntityShells()
            val relatedAnimesEntities = response.relatedAnimeEntityShells()
            val relatedMangasEntities = response.relatedMangaEntityShells()
            val screenshotEntities = response.animeScreenshotEntityShells()
            val videosEntities = response.animeVideoEntityShells()

            val genreCrossRefs = response.animeGenresCrossReferences()
            val studioCrossRefs = response.animeStudioCrossRefs()
            val animeRelatedEntities = response.animeRelatedEntities()

            databaseTransactionRunner {
                animeDao.upsertAnime(animeEntity)
                animeDetailsDao.upsertAnimeDetails(animeDetailsEntity)

                animeDetailsDao.deleteAnimeGenreCrossReferences(animeId)
                animeDetailsDao.deleteAnimeStudioCrossReferences(animeId)
                animeDetailsDao.deleteAnimeRelated(animeId)
                animeDetailsDao.deleteAnimeScreenshots(animeId)
                animeDetailsDao.deleteAnimeVideos(animeId)

                genresEntities?.let { genreDao.insertOrIgnoreGenres(it) }
                genreCrossRefs?.let { animeDetailsDao.insertAnimeGenreCrossReferences(it) }

                animeDetailsDao.insertOrIgnoreStudios(studiosEntities)
                animeDetailsDao.insertAnimeStudioCrossReferences(studioCrossRefs)

                relatedAnimesEntities?.let { animeDao.upsertAnimes(it) }
                relatedMangasEntities?.let { mangaDao.upsertMangas(it) }
                animeRelatedEntities?.let { animeDetailsDao.insertAnimeRelated(it) }

                animeDetailsDao.insertAnimeScreenshots(screenshotEntities)
                videosEntities?.let { animeDetailsDao.insertAnimeVideos(it) }
            }
        }
    }

    suspend fun refreshAnimeRoles(animeId: Long) {
        withContext(ioDispatcher) {
            val response = animeDataSource.getAnimeRoles(animeId)

            val personEntities = response.personEntityShells()
            val authorRolesEntities = response.animePersonRolesEntities(animeId)

            val characterEntities = response.characterEntityShells()
            val animeCharacterEntities = response.animeCharacterEntities(animeId)

            databaseTransactionRunner {
                animeDetailsDao.deleteAnimePersonRoles(animeId)
                animeDetailsDao.deleteAnimeCharacters(animeId)

                personEntities?.let { personDao.insertOrReplacePersons(it) }
                authorRolesEntities?.let { animeDetailsDao.insertAnimeAuthors(it) }
                characterEntities?.let { characterDao.insertOrReplaceCharacters(it) }
                animeCharacterEntities?.let { animeDetailsDao.insertAnimeCharacters(it) }
            }
        }
    }

    suspend fun refreshSimilarAnimes(animeId: Long) {
        withContext(ioDispatcher) {
            val response = animeDataSource.getSimilarAnimes(animeId)

            val animes = response.map { it.asEntity() }
            val crossRefs = response.map { AnimeSimilarAnimeCrossRef(animeId, it.id) }

            databaseTransactionRunner {
                animeDetailsDao.deleteAnimeSimilarAnimeCrossRef(animeId)

                animeDao.upsertAnimes(animes)
                animeDetailsDao.insertAnimeSimilarAnimeCrossReferences(crossRefs)
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
            queryMap = mapOf(
                QueryMapKey.Status to EntryStatus.Ongoing.serializedName,
                QueryMapKey.Order to Order.Popularity.serializedValue,
                QueryMapKey.MyList to "!${UserRateStatus.Watching.serializedName},!${UserRateStatus.Rewatching.serializedName}"
            )
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
        animeDao.upsertAnimes(animes)
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
                    animeDao.upsertAnimes(animeEntities)
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

private const val INITIAL_PAGE = 1