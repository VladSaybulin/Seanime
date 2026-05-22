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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.data.model.animeCharacterEntities
import ru.vladsaybulin.data.model.animeGenresCrossReferences
import ru.vladsaybulin.data.model.animePersonRolesEntities
import ru.vladsaybulin.data.model.animeRelatedEntities
import ru.vladsaybulin.data.model.animeScreenshotEntityShells
import ru.vladsaybulin.data.model.animeStudioCrossRefs
import ru.vladsaybulin.data.model.animeVideoEntityShells
import ru.vladsaybulin.data.model.asAnimeDetailsEntity
import ru.vladsaybulin.data.model.asAnimeEntity
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.characterEntityShells
import ru.vladsaybulin.data.model.genreEntityShells
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.relatedAnimeEntityShells
import ru.vladsaybulin.data.model.relatedMangaEntityShells
import ru.vladsaybulin.data.model.studioEntityShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.UpdateScope
import ru.vladsaybulin.data.request.cachedKey
import ru.vladsaybulin.data.util.AbstractShikimoriPagingSource
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
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeDetails
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.Order
import ru.vladsaybulin.model.search.QueryMapKey
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
    private val coordinator: RequestCoordinator,
    @param:Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DomainAnimeRepository {
    override fun animeSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Anime> =
        SearchPagingSource { page, limit -> loadSearchAnimePage(page, limit, queryMap) }

    override fun getOngoingAnimesStream(limit: Int): Flow<List<Anime>> =
        ongoingAnimeDao.getOngoingAnime(limit)
            .map { it.map(AnimeEntity::asExternalModel) }

    override fun getAnimeDetailsStream(animeId: Long): Flow<AnimeDetails> =
        animeDetailsDao.getAnimeDetails(animeId).map { it.asExternalModel() }

    override fun getAnimeMainCharactersStream(animeId: Long): Flow<List<Character>> =
        animeDetailsDao.getMainAnimeCharacters(animeId).map { mainCharacters ->
            mainCharacters.map { it.asExternalModel().character }
        }

    override fun getAnimeMainAuthorsStream(animeId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getMainAnimeAuthors(animeId).map { it.map(PopulatedAnimeAuthor::asExternalModel) }

    override fun getFirstAnimeRelatedStream(animeId: Long, limit: Int): Flow<List<RelatedTitle>> =
        animeDetailsDao.getFirstAnimeRelated(animeId, limit).map { it.map(PopulatedAnimeRelated::asExternalModel) }

    override fun getAnimeScreenshots(animeId: Long): Flow<List<Image>> =
        animeDetailsDao.getAnimeScreenshots(animeId).map { it.map(AnimeScreenshotEntity::asExternalModel) }

    override fun getFirstAnimeVideos(animeId: Long, limit: Int): Flow<List<Video>> =
        animeDetailsDao.getFirstAnimeVideos(animeId, limit).map { it.map(AnimeVideoEntity::asExternalModel) }

    override fun getSimilarAnimes(animeId: Long): Flow<List<Anime>> =
        animeDetailsDao.getSimilarAnimes(animeId).map { it.map(PopulatedSimilarAnime::asExternalModel) }

    override fun getAllAnimeAuthors(animeId: Long): Flow<List<PersonWithRoles>> =
        animeDetailsDao.getAllAnimeAuthors(animeId)
            .map { it.map(PopulatedAnimeAuthor::asExternalModel) }

    override fun getAllAnimeRelatedTitles(animeId: Long): Flow<List<RelatedTitle>> =
        animeDetailsDao.getAllAnimeRelatedTitles(animeId)
            .map { it.map(PopulatedAnimeRelated::asExternalModel) }

    override fun getAllAnimeCharacters(animeId: Long): Flow<List<CharacterWithRole>> =
        animeDetailsDao.getAllAnimeCharacters(animeId)
            .map { it.map(PopulatedAnimeCharacter::asExternalModel) }

    override fun getAllAnimeVideos(animeId: Long): Flow<List<Video>> =
        animeDetailsDao.getAllAnimeVideos(animeId)
            .map { it.map(AnimeVideoEntity::asExternalModel) }

    override suspend fun refreshAnimeDetails(animeId: Long, force: Boolean) {
        coordinator.sync(
            key = cachedKey(RequestType.Anime, animeId),
            forceRefresh = force,
            ttlStrategy = TTLStrategies.TitleDetails,
            block = { updateAnimeDetails(animeId) },
        )
    }

    override suspend fun refreshAnimeRoles(animeId: Long, force: Boolean) {
        coordinator.sync(
            key = cachedKey(RequestType.AnimeRoles, animeId),
            forceRefresh = force,
            ttlStrategy = TTLStrategies.TitleDetails,
            block = { updateAnimeRoles(animeId) }
        )
    }

    override suspend fun refreshSimilarAnimes(animeId: Long, force: Boolean) {
        coordinator.sync(
            key = cachedKey(RequestType.SimilarAnimes, animeId),
            forceRefresh = force,
            ttlStrategy = TTLStrategies.TitleDetails,
            block = { updateSimilarAnimes(animeId) }
        )
    }

    override suspend fun refreshOngoingAnimes(limit: Int, force: Boolean) = syncHelper.sync(
        request = Request(RequestType.OngoingAnimes),
        forceRefresh = force,
        update = { updateOngoingAnimes(limit) }
    )

    private suspend fun UpdateScope.updateAnimeDetails(animeId: Long) {
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

        write {
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

    private suspend fun UpdateScope.updateAnimeRoles(animeId: Long) = withContext(ioDispatcher) {
        val response = animeDataSource.getAnimeRoles(animeId)

        val personEntities = response.personEntityShells()
        val authorRolesEntities = response.animePersonRolesEntities(animeId)

        val characterEntities = response.characterEntityShells()
        val animeCharacterEntities = response.animeCharacterEntities(animeId)

        write {
            animeDetailsDao.deleteAnimePersonRoles(animeId)
            animeDetailsDao.deleteAnimeCharacters(animeId)

            personEntities?.let { personDao.insertOrReplacePersons(it) }
            authorRolesEntities?.let { animeDetailsDao.insertAnimeAuthors(it) }
            characterEntities?.let { characterDao.insertOrReplaceCharacters(it) }
            animeCharacterEntities?.let { animeDetailsDao.insertAnimeCharacters(it) }
        }
    }

    private suspend fun UpdateScope.updateSimilarAnimes(animeId: Long) = withContext(ioDispatcher) {
        val response = animeDataSource.getSimilarAnimes(animeId)

        val animes = response.map { it.asEntity() }
        val crossRefs = response.map { AnimeSimilarAnimeCrossRef(animeId, it.id) }

        write {
            animeDetailsDao.deleteAnimeSimilarAnimeCrossRef(animeId)

            animeDao.insertOrIgnoreAnimes(animes)
            animeDetailsDao.insertAnimeSimilarAnimeCrossReferences(crossRefs)
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

    private fun getPagedAnimePagingSource(queryMap: Map<QueryMapKey, String>) =
        object : AbstractShikimoriPagingSource<Anime>() {
            override suspend fun loadPage(
                pageNumber: Int,
                pageSize: Int
            ): LoadResult<Int, Anime> = try {
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

                val animes = networkAnimes.map { it.asExternalModel() }

                LoadResult.Page(
                    data = animes,
                    nextKey = if (animes.size == pageSize) pageNumber + 1 else null,
                    prevKey = null
                )
            } catch (e: Exception) {
                LoadResult.Error(e)
            }

        }

    private fun List<NetworkAnime>.shuffledAnimeOngoings(): List<NetworkAnime> {
        val seed = Clock.System.now().toEpochMilliseconds() / MILLISECONDS_IN_DAY
        return shuffled(Random(seed))
    }
}

private const val MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000

private const val INITIAL_PAGE = 1