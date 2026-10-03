/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.data.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.common.ui.tryRefresh
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.data.di.DataScope
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asTitle
import ru.vladsaybulin.data.model.extractAnimeCharacters
import ru.vladsaybulin.data.model.extractAnimePersons
import ru.vladsaybulin.data.model.extractGenreEntities
import ru.vladsaybulin.data.model.extractRelatedEntities
import ru.vladsaybulin.data.model.extractScreenshotEntities
import ru.vladsaybulin.data.model.extractStudiosEntities
import ru.vladsaybulin.data.model.extractVideoEntities
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.UpdateScope
import ru.vladsaybulin.data.request.cachedKey
import ru.vladsaybulin.data.withForceStrategy
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.AnimeDetailsDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.OngoingAnimeDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.StudioDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.anime.AnimeCharacterReferenceWithRoleEntity
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.anime.AnimeGenreCrossRef
import ru.vladsaybulin.database.models.anime.AnimePersonReferenceWithRolesEntity
import ru.vladsaybulin.database.models.anime.AnimeRelatedEntity
import ru.vladsaybulin.database.models.anime.AnimeScreenshotEntity
import ru.vladsaybulin.database.models.anime.AnimeSimilarAnimeCrossRef
import ru.vladsaybulin.database.models.anime.AnimeStudioCrossRef
import ru.vladsaybulin.database.models.anime.AnimeVideoEntity
import ru.vladsaybulin.database.models.anime.OngoingAnimeEntity
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.anime.asExternalModel
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.title.TitleDetails
import ru.vladsaybulin.network.datasource.AnimeDataSource
import ru.vladsaybulin.network.models.anime.NetworkAnime
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random
import ru.vladsaybulin.core.domain.repository.AnimeRepository as DomainAnimeRepository

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
    private val studioDao: StudioDao,
    private val coordinator: RequestCoordinator,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    @DataScope private val scope: CoroutineScope
) : DomainAnimeRepository {
    override fun getTitleBrief(titleId: Long): Flow<Title> =
        animeDao.getAnimeStreamById(titleId)
            .filterNotNull()
            .map { brief ->
                brief.asTitle()
            }

    override fun getTitleDetails(titleId: Long): Flow<TitleDetails> =
        animeDetailsDao.getDetailsStream(titleId)
            .filterNotNull()
            .map { details ->
                details.asExternalModel()
            }

    override fun getRelatedTitles(titleId: Long): Flow<List<RelatedTitle>> =
        animeDetailsDao.getRelatedStream(titleId).map { relatedEntities ->
            relatedEntities.map { it.asExternalModel() }
        }

    override fun getTitleCharacters(titleId: Long): Flow<List<CharacterWithRole>> =
        animeDetailsDao.getCharactersWithRoleStream(titleId).map { characters ->
            characters.map { it.asExternalModel() }
        }

    override fun getTitleAuthors(titleId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getAuthorsWithRolesStream(titleId).map { authors ->
            authors.map { it.asExternalModel() }
        }

    override fun getSimilarTitles(titleId: Long): Flow<List<Title>> =
        animeDetailsDao.getSimilarStream(titleId).map { similarEntities ->
            similarEntities.map { it.asTitle() }
        }

    override fun refreshTitleDetails(titleId: Long, forceRefresh: Boolean): Flow<Throwable> = callbackFlow {
        val monitorJob = launch {
            launchSyncAnimeDetails(titleId, forceRefresh, ::trySend)
            close()
        }

        awaitClose { monitorJob.cancel() }
    }

    override fun animeSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Anime> =
        SearchPagingSource { page, limit -> loadSearchAnimePage(page, limit, queryMap) }

    override fun getOngoingAnimesStream(limit: Int): Flow<List<Anime>> =
        ongoingAnimeDao.getOngoingAnime(limit)
            .map { it.map(AnimeEntity::asExternalModel) }

    override fun getAnimePosterStream(animeId: Long): Flow<Image?> =
        animeDao.getPosterStream(animeId).map { it?.asExternalModel() }

    override fun getAnimeScreenshotsStream(animeId: Long): Flow<List<Image>> =
        animeDetailsDao.getScreenshotsStream(animeId)
            .map { screenshots ->
                screenshots.map { it.asExternalModel() }
            }

    override fun getAnimeVideos(animeId: Long): Flow<List<Video>> {
        return animeDetailsDao.getVideosStream(animeId)
            .map { videos ->
                videos.map { it.asExternalModel() }
            }
    }

    override suspend fun refreshOngoingAnimes(limit: Int, force: Boolean) {
        coordinator.sync(
            key = cachedKey(RequestType.OngoingAnimes),
            ttlPolicy = withForceStrategy(force) { TTLStrategies.OngoingAnimes },
            block = { updateOngoingAnimes(limit) }
        )
    }

    private fun launchSyncAnimeDetails(
        animeId: Long,
        forceRefresh: Boolean,
        catch: (Throwable) -> Unit
    ) = scope.launch {
        val briefJob = launch {
            tryRefresh(catch = catch) {
                syncAnimeBrief(animeId, forceRefresh)
            }
        }

        val detailsJob = launch {
            tryRefresh(catch = catch) {
                syncAnimeDetails(animeId, forceRefresh, briefJob)
            }
        }

        launch {
            tryRefresh(catch = catch) {
                syncAnimeRoles(animeId, forceRefresh, detailsJob)
            }
        }

        launch {
            tryRefresh(catch = catch) {
                syncSimilarAnime(animeId, forceRefresh, detailsJob)
            }
        }
    }

    private suspend fun syncAnimeBrief(animeId: Long, forceRefresh: Boolean) {
        val needRefresh = forceRefresh || !animeDao.hasAnime(animeId)
        if (!needRefresh) return

        val anime = animeDataSource.getAnimeById(animeId)
        val entity = anime.asEntity()
        animeDao.upsertAnime(entity)
    }

    private suspend fun syncAnimeDetails(animeId: Long, forceRefresh: Boolean, briefJob: Job) = coordinator.sync(
        key = cachedKey(RequestType.Anime, animeId),
        ttlPolicy = withForceStrategy(forceRefresh) { TTLStrategies.TitleDetails },
    ) {
        val details = animeDataSource.getAnimeDetails(animeId)

        val genreEntities = mutableListOf<GenreEntity>()
        val genreCrossReferences = mutableListOf<AnimeGenreCrossRef>()
        val studioEntities = mutableListOf<StudioEntity>()
        val studioCrossReferences = mutableListOf<AnimeStudioCrossRef>()
        val animeEntities = mutableListOf<AnimeEntity>()
        val mangaEntities = mutableListOf<MangaEntity>()
        val relatedTitleReferences = mutableListOf<AnimeRelatedEntity>()
        val screenshotEntities = mutableListOf<AnimeScreenshotEntity>()
        val videosEntities = mutableListOf<AnimeVideoEntity>()

        details.extractGenreEntities(genreEntities, genreCrossReferences)
        details.extractStudiosEntities(studioEntities, studioCrossReferences)
        details.extractRelatedEntities(animeEntities, mangaEntities, relatedTitleReferences)
        details.extractScreenshotEntities(screenshotEntities)
        details.extractVideoEntities(videosEntities)

        val detailsEntity = details.asEntity()
        val userRateEntity = details.userRate?.asEntity(animeId = animeId)

        // Ensure that the brief data is written before writing details to avoid violating relationships
        briefJob.join()

        write {
            animeDao.upsertAnimes(animeEntities)
            mangaDao.upsertMangas(mangaEntities)
            genreDao.insertOrIgnoreGenres(genreEntities)
            studioDao.insertOrIgnore(studioEntities)

            animeDetailsDao.insertOrReplaceDetails(detailsEntity)
            animeDetailsDao.insertDetailsReferences(
                relatedTitles = relatedTitleReferences,
                genreReferences = genreCrossReferences,
                studioReferences = studioCrossReferences,
                screenshots = screenshotEntities,
                videos = videosEntities
            )

            userRateEntity?.let { userRateDao.upsertUserRate(it) }
        }
    }

    private suspend fun syncAnimeRoles(animeId: Long, forceRefresh: Boolean, detailsJob: Job) = coordinator.sync(
        key = cachedKey(RequestType.AnimeRoles, animeId),
        ttlPolicy = withForceStrategy(forceRefresh) { TTLStrategies.TitleDetails },
    ) {
        val roles = animeDataSource.getAnimeRoles(animeId)

        val characterEntities = mutableListOf<CharacterEntity>()
        val animeCharacterEntities = mutableListOf<AnimeCharacterReferenceWithRoleEntity>()
        val personEntities = mutableListOf<PersonEntity>()
        val animePersonEntities = mutableListOf<AnimePersonReferenceWithRolesEntity>()

        roles.extractAnimeCharacters(animeId, characterEntities, animeCharacterEntities)
        roles.extractAnimePersons(animeId, personEntities, animePersonEntities)

        // Ensure that the details data is written before writing roles to avoid violating relationships
        detailsJob.join()

        write {
            characterDao.insertOrReplaceCharacters(characterEntities)
            personDao.insertOrReplacePersons(personEntities)
            animeDetailsDao.insertRolesReferences(
                characterReferences = animeCharacterEntities,
                personReferences = animePersonEntities
            )
        }
    }

    private suspend fun syncSimilarAnime(animeId: Long, forceRefresh: Boolean, detailsJob: Job) = coordinator.sync(
        key = cachedKey(RequestType.SimilarAnimes, animeId),
        ttlPolicy = withForceStrategy(forceRefresh) { TTLStrategies.TitleDetails },
    ) {
        val similarAnimes = animeDataSource.getSimilarAnimes(animeId)

        val animeEntities = similarAnimes.map(NetworkAnime::asEntity)
        val similarReferences = similarAnimes.map { anime ->
            AnimeSimilarAnimeCrossRef(
                animeId = animeId,
                similarAnimeId = anime.id
            )
        }

        // Ensure that the details data is written before writing similar animes to avoid violating relationships
        detailsJob.join()

        write {
            animeDao.upsertAnimes(animeEntities)
            animeDetailsDao.insertSimilarReferences(similarReferences)
        }
    }

    private suspend fun UpdateScope.updateOngoingAnimes(limit: Int) {
        val response = animeDataSource.getAnime(
            page = 1,
            limit = 50,
            queryMap = mapOf(
                QueryMapKey.Status to EntryStatus.Ongoing.serializedName,
                QueryMapKey.Order to Order.Popularity.serializedValue
            )
        )
            .shuffledAnimeOngoings()
            .let { it.subList(0, limit.coerceAtMost(it.size)) }

        val animes = response.map(NetworkAnime::asEntity)
        val ongoingAnime = animes.map { OngoingAnimeEntity(animeId = it.id) }

        write {
            animeDao.upsertAnimes(animes)
            ongoingAnimeDao.deleteAll()
            ongoingAnimeDao.insertAll(ongoingAnime)
        }
    }

    private suspend fun loadSearchAnimePage(
        page: Int,
        limit: Int,
        queryMap: Map<QueryMapKey, String>
    ): List<Anime> = withContext(ioDispatcher) {
        val networkAnimes = animeDataSource.getAnime(
            page = page,
            limit = limit,
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

        animeEntities.map { it.asExternalModel() }
    }

    private fun List<NetworkAnime>.shuffledAnimeOngoings(): List<NetworkAnime> {
        val seed = Clock.System.now().toEpochMilliseconds() / MILLISECONDS_IN_DAY
        return shuffled(Random(seed))
    }
}

private const val MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000