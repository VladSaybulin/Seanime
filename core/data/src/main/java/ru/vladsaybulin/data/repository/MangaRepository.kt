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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.common.ui.tryRefresh
import ru.vladsaybulin.core.domain.titledetails.TitleDetailsStreams
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.data.di.DataScope
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.model.asTitle
import ru.vladsaybulin.data.model.extractGenreEntities
import ru.vladsaybulin.data.model.extractMangaCharacters
import ru.vladsaybulin.data.model.extractMangaPersons
import ru.vladsaybulin.data.model.extractPublisherEntities
import ru.vladsaybulin.data.model.extractRelatedEntities
import ru.vladsaybulin.data.model.mergeMangaDetailsToExternalModel
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.cachedKey
import ru.vladsaybulin.data.withForceStrategy
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.MangaDetailsDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.PublisherDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.common.asExternalModel
import ru.vladsaybulin.database.models.genre.GenreEntity
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.database.models.manga.MangaCharacterReferenceWithRoleEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaGenreCrossRef
import ru.vladsaybulin.database.models.manga.MangaPersonReferenceWithRolesEntity
import ru.vladsaybulin.database.models.manga.MangaPublisherCrossRef
import ru.vladsaybulin.database.models.manga.MangaRelatedEntity
import ru.vladsaybulin.database.models.manga.MangaSimilarMangaCrossRef
import ru.vladsaybulin.database.models.manga.PublisherEntity
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.userrate.asExternalModel
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.search.QueryMapKey
import ru.vladsaybulin.model.title.TitleRoles
import ru.vladsaybulin.network.datasource.MangaDataSource
import javax.inject.Inject
import ru.vladsaybulin.core.domain.repository.MangaRepository as DomainMangaRepository

class MangaRepository @Inject constructor(
    private val mangaDataSource: MangaDataSource,
    private val animeDao: AnimeDao,
    private val userRateDao: UserRateDao,
    private val mangaDetailsDao: MangaDetailsDao,
    private val personDao: PersonDao,
    private val characterDao: CharacterDao,
    private val mangaDao: MangaDao,
    private val genreDao: GenreDao,
    private val publisherDao: PublisherDao,
    private val coordinator: RequestCoordinator,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    @DataScope private val scope: CoroutineScope
) : DomainMangaRepository {
    override fun getMangaDetailsStreams(mangaId: Long, forceRefresh: Boolean): TitleDetailsStreams {
        val errors = MutableSharedFlow<Throwable>(extraBufferCapacity = 1)

        launchSyncMangaDetails(mangaId, forceRefresh, errors)

        return TitleDetailsStreams(
            brief = mangaDao.getMangaStreamById(mangaId).map { it.asTitle() },
            details = mangaDetailsDao.getDetailsStream(mangaId)
                .map { details ->
                     val related = mangaDetailsDao.getRelated(mangaId)
                    mergeMangaDetailsToExternalModel(details, related)
                },
            roles = mangaDetailsDao.getCharactersWithRoleStream(mangaId)
                .map { characters ->
                    val authors = mangaDetailsDao.getAuthorsWithRoles(mangaId)
                    TitleRoles(
                        authors = authors.map { it.asExternalModel() },
                        characters = characters.map { it.asExternalModel() }
                    )
                },
            similar = mangaDetailsDao.getSimilarStream(mangaId)
                .map { similar ->
                    similar.map { it.asTitle() }
                },
            userRate = userRateDao.getUserRate(mangaId).map { it?.asExternalModel() },
            errors = errors
        )
    }

    override fun mangaSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Manga> =
        SearchPagingSource { page, limit -> loadMangaSearchPage(page, limit, queryMap) }

    override fun getMangaPosterStream(mangaId: Long): Flow<Image?> =
        mangaDao.getPosterStream(mangaId).map { it?.asExternalModel() }

    private fun launchSyncMangaDetails(
        mangaId: Long,
        forceRefresh: Boolean,
        errors: MutableSharedFlow<Throwable>
    ) = scope.launch {
        val catching: suspend (Throwable) -> Unit = { errors.emit(it) }

        val briefJob = launch {
            tryRefresh(catch = catching) {
                syncMangaBrief(mangaId, forceRefresh)
            }
        }

        val detailsJob = launch {
            tryRefresh(catch = catching) {
                syncMangaDetails(mangaId, forceRefresh, briefJob)
            }
        }

        launch {
            tryRefresh(catch = catching) {
                syncMangaRoles(mangaId, forceRefresh, detailsJob)
            }
        }

        launch {
            tryRefresh(catch = catching) {
                syncSimilarManga(mangaId, forceRefresh, detailsJob)
            }
        }
    }

    private suspend fun syncMangaBrief(mangaId: Long, forceRefresh: Boolean) {
        val needRefresh = forceRefresh || mangaDao.hasManga(mangaId)
        if (!needRefresh) return

        val networkManga = mangaDataSource.getMangaById(mangaId)
        val mangaEntity = networkManga.asEntity()
        mangaDao.upsertManga(mangaEntity)
    }

    private suspend fun syncMangaDetails(mangaId: Long, forceRefresh: Boolean, briefJob: Job) = coordinator.sync(
        key = cachedKey(RequestType.Manga, mangaId),
        ttlStrategy = withForceStrategy(forceRefresh) { TTLStrategies.TitleDetails },
    ) {
        val details = mangaDataSource.getMangaDetailsById(mangaId)

        val genreEntities = mutableListOf<GenreEntity>()
        val genreCrossReferences = mutableListOf<MangaGenreCrossRef>()
        val publisherEntities = mutableListOf<PublisherEntity>()
        val publisherCrossReferences = mutableListOf<MangaPublisherCrossRef>()
        val animeEntities = mutableListOf<AnimeEntity>()
        val mangaEntities = mutableListOf<MangaEntity>()
        val relatedTitleReferences = mutableListOf<MangaRelatedEntity>()

        details.extractGenreEntities(genreEntities, genreCrossReferences)
        details.extractPublisherEntities(publisherEntities, publisherCrossReferences)
        details.extractRelatedEntities(mangaEntities, animeEntities, relatedTitleReferences)

        val detailsEntity = details.asEntity()
        val userRateEntity = details.userRate?.asEntity()

        // Ensure that the brief data is written before writing details to avoid violating relationships
        briefJob.join()

        write {
            animeDao.upsertAnimes(animeEntities)
            mangaDao.upsertMangas(mangaEntities)
            genreDao.insertOrIgnoreGenres(genreEntities)
            publisherDao.insertOrIgnore(publisherEntities)

            mangaDetailsDao.insertOrReplaceDetails(detailsEntity)
            mangaDetailsDao.insertDetailsReferences(
                relatedTitles = relatedTitleReferences,
                genreReferences = genreCrossReferences,
                publisherReferences = publisherCrossReferences
            )

            userRateEntity?.let { userRateDao.insertOrReplaceUserRate(it) }
        }
    }

    private suspend fun syncMangaRoles(mangaId: Long, forceRefresh: Boolean, detailsJob: Job) = coordinator.sync(
        key = cachedKey(RequestType.MangaRoles, mangaId),
        ttlStrategy = withForceStrategy(forceRefresh) { TTLStrategies.TitleDetails },
    ) {
        val roles = mangaDataSource.getMangaRolesById(mangaId)

        val characterEntities = mutableListOf<CharacterEntity>()
        val mangaCharacterEntities = mutableListOf<MangaCharacterReferenceWithRoleEntity>()
        val personEntities = mutableListOf<PersonEntity>()
        val mangaPersonEntities = mutableListOf<MangaPersonReferenceWithRolesEntity>()

        roles.extractMangaCharacters(mangaId, characterEntities, mangaCharacterEntities)
        roles.extractMangaPersons(mangaId, personEntities, mangaPersonEntities)

        // Ensure that the details data is written before writing similar animes to avoid violating relationships
        detailsJob.join()

        write {
            characterDao.insertOrReplaceCharacters(characterEntities)
            personDao.insertOrReplacePersons(personEntities)
            mangaDetailsDao.insertRolesReferences(
                characterReferences = mangaCharacterEntities,
                personReferences = mangaPersonEntities
            )
        }
    }

    private suspend fun syncSimilarManga(mangaId: Long, forceRefresh: Boolean, detailsJob: Job) = coordinator.sync(
        key = cachedKey(RequestType.SimilarMangas, mangaId),
        ttlStrategy = withForceStrategy(forceRefresh) { TTLStrategies.TitleDetails },
    ) {
        val similarMangas = mangaDataSource.getSimilarManga(mangaId)

        val mangaEntities = similarMangas.map { it.asEntity() }
        val similarReferences = similarMangas.map { manga ->
            MangaSimilarMangaCrossRef(
                mangaId = mangaId,
                similarMangaId = manga.id
            )
        }

        // Ensure that the details data is written before writing similar animes to avoid violating relationships
        detailsJob.join()

        write {
            mangaDao.insertOrReplaceMangas(mangaEntities)
            mangaDetailsDao.insertSimilarReferences(similarReferences)
        }
    }

    private suspend fun loadMangaSearchPage(
        page: Int,
        limit: Int,
        queryMap: Map<QueryMapKey, String>
    ): List<Manga> = withContext(ioDispatcher) {
        val networkMangas = mangaDataSource.getManga(
            page = page,
            limit = limit,
            queryMap = queryMap
        )
        val mangaEntities = networkMangas.map { it.asEntity() }
        val userRatesEntities = networkMangas.mapNotNull { it.userRateEntityShell() }

        if (mangaEntities.isNotEmpty()) {
            mangaDao.insertOrReplaceMangas(mangaEntities)
        }

        if (userRatesEntities.isNotEmpty()) {
            userRateDao.insertOrReplaceUserRates(userRatesEntities)
        }

        mangaEntities.map(MangaEntity::asExternalModel)
    }
}