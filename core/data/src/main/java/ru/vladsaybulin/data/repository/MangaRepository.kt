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
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.asMangaDetailsEntity
import ru.vladsaybulin.data.model.asMangaEntity
import ru.vladsaybulin.data.model.characterEntityShells
import ru.vladsaybulin.data.model.genreEntityShells
import ru.vladsaybulin.data.model.mangaCharacterEntities
import ru.vladsaybulin.data.model.mangaGenreCrossReferences
import ru.vladsaybulin.data.model.mangaPersonRolesEntities
import ru.vladsaybulin.data.model.mangaPublisherCrossRefs
import ru.vladsaybulin.data.model.mangaRelatedEntities
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.publisherEntityShells
import ru.vladsaybulin.data.model.relatedAnimeEntityShells
import ru.vladsaybulin.data.model.relatedMangaEntityShells
import ru.vladsaybulin.data.model.userRateEntityShell
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.GenreDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.MangaDetailsDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.manga.MangaSimilarMangaCrossRef
import ru.vladsaybulin.database.models.manga.PopulatedMangaAuthor
import ru.vladsaybulin.database.models.manga.PopulatedMangaCharacter
import ru.vladsaybulin.database.models.manga.PopulatedMangaRelated
import ru.vladsaybulin.database.models.manga.PopulatedSimilarManga
import ru.vladsaybulin.database.models.manga.asExternalModel
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaDetails
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.search.QueryMapKey
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
    private val databaseTransactionRunner: DatabaseTransactionRunner,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DomainMangaRepository {

    override fun mangaSearchPagingSource(queryMap: Map<QueryMapKey, String>): PagingSource<Int, Manga> =
        SearchPagingSource { page, limit -> loadMangaSearchPage(page, limit, queryMap) }

    override fun getMangaDetailsStream(mangaId: Long): Flow<MangaDetails> =
        mangaDetailsDao.getMangaDetails(mangaId).map { it.asExternalModel() }

    override fun getFirstMangaRelatedStream(mangaId: Long, limit: Int): Flow<List<RelatedTitle>> =
        mangaDetailsDao.getFirstMangaRelated(mangaId, limit)
            .map { it.map(PopulatedMangaRelated::asExternalModel) }

    override fun getMangaMainCharactersStream(mangaId: Long): Flow<List<Character>> =
        mangaDetailsDao.getMainMangaCharacters(mangaId)
            .map { entities -> entities.map { it.asExternalModel().character } }

    override fun getMangaMainAuthorsStream(mangaId: Long): Flow<List<PersonWithRoles>> =
        mangaDetailsDao.getMainMangaAuthors(mangaId)
            .map { it.map(PopulatedMangaAuthor::asExternalModel) }

    override fun getSimilarMangasStream(mangaId: Long): Flow<List<Manga>> =
        mangaDetailsDao.getSimilarMangas(mangaId)
            .map { it.map(PopulatedSimilarManga::asExternalModel) }

    override fun getAllMangaAuthors(mangaId: Long): Flow<List<PersonWithRoles>> =
        mangaDetailsDao.getAllMangaAuthors(mangaId)
            .map { it.map(PopulatedMangaAuthor::asExternalModel) }

    override suspend fun refreshMangaDetails(mangaId: Long) {
        withContext(ioDispatcher) {
            val response = mangaDataSource.getMangaDetails(mangaId)

            val mangaEntity = response.asMangaEntity()
            val mangaDetailsEntity = response.asMangaDetailsEntity()

            val genresEntities = response.genreEntityShells()
            val publishersEntities = response.publisherEntityShells()
            val relatedAnimesEntities = response.relatedAnimeEntityShells()
            val relatedMangasEntities = response.relatedMangaEntityShells()

            val genreCrossRefs = response.mangaGenreCrossReferences()
            val studioCrossRefs = response.mangaPublisherCrossRefs()
            val mangaRelatedEntities = response.mangaRelatedEntities()

            databaseTransactionRunner {
                mangaDao.upsertManga(mangaEntity)
                mangaDetailsDao.upsertMangaDetails(mangaDetailsEntity)

                mangaDetailsDao.deleteMangaGenreCrossReferences(mangaId)
                mangaDetailsDao.deleteMangaPublisherCrossReferences(mangaId)
                mangaDetailsDao.deleteMangaRelated(mangaId)

                genresEntities?.let { genreDao.insertOrIgnoreGenres(it) }
                genreCrossRefs?.let { mangaDetailsDao.insertMangaGenreCrossReferences(it) }

                mangaDetailsDao.insertOrIgnorePublishers(publishersEntities)
                mangaDetailsDao.insertMangaPublisherCrossReferences(studioCrossRefs)

                relatedAnimesEntities?.let { animeDao.upsertAnimes(it) }
                relatedMangasEntities?.let { mangaDao.upsertMangas(it) }
                mangaRelatedEntities?.let { mangaDetailsDao.insertMangaRelated(it) }
            }
        }
    }

    override suspend fun refreshMangaRoles(mangaId: Long) {
        withContext(ioDispatcher) {
            val response = mangaDataSource.getMangaRoles(mangaId)

            val personEntities = response.personEntityShells()
            val authorRolesEntities = response.mangaPersonRolesEntities(mangaId)

            val characterEntities = response.characterEntityShells()
            val animeCharacterEntities = response.mangaCharacterEntities(mangaId)

            databaseTransactionRunner {
                mangaDetailsDao.deleteMangaPersonRoles(mangaId)
                mangaDetailsDao.deleteMangaCharacters(mangaId)

                personEntities?.let { personDao.insertOrReplacePersons(it) }
                authorRolesEntities?.let { mangaDetailsDao.insertMangaAuthors(it) }
                characterEntities?.let { characterDao.insertOrReplaceCharacters(it) }
                animeCharacterEntities?.let { mangaDetailsDao.insertMangaCharacters(it) }
            }
        }
    }

    override suspend fun refreshSimilarMangas(mangaId: Long) {
        withContext(ioDispatcher) {
            val response = mangaDataSource.getSimilarManga(mangaId)

            val mangas = response.map { it.asEntity() }
            val crossRefs = response.map { MangaSimilarMangaCrossRef(mangaId, it.id) }

            databaseTransactionRunner {
                mangaDetailsDao.deleteMangaSimilarMangaCrossRef(mangaId)

                mangaDao.insertOrIgnoreMangas(mangas)
                mangaDetailsDao.insertMangaSimilarMangaCrossReferences(crossRefs)
            }
        }
    }

    private suspend fun loadMangaSearchPage(
        page: Int,
        limit: Int,
        queryMap: Map<QueryMapKey, String>
    ): List<Manga> {
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

        return mangaEntities.map(MangaEntity::asExternalModel)
    }

    override fun getAllMangaRelatedTitles(mangaId: Long): Flow<List<RelatedTitle>> =
        mangaDetailsDao.getAllMangaRelatedTitles(mangaId)
            .map { it.map(PopulatedMangaRelated::asExternalModel) }

    override fun getAllMangaCharacters(mangaId: Long): Flow<List<CharacterWithRole>> =
        mangaDetailsDao.getAllMangaCharacters(mangaId)
            .map { it.map(PopulatedMangaCharacter::asExternalModel) }
}