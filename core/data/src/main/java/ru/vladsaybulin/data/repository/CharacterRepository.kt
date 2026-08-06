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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.data.model.animeCrossRefs
import ru.vladsaybulin.data.model.animeEntityShells
import ru.vladsaybulin.data.model.asDetailsEntity
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.mangaCrossRefs
import ru.vladsaybulin.data.model.mangaEntityShells
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.seyuCrossRefs
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.UpdateScope
import ru.vladsaybulin.data.request.cachedKey
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.models.character.asExternalModel
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.model.character.CharacterDetails
import ru.vladsaybulin.network.datasource.CharacterDataSource
import javax.inject.Inject
import ru.vladsaybulin.core.domain.repository.CharacterRepository as DomainCharacterRepository

class CharacterRepository @Inject constructor(
    private val characterDataSource: CharacterDataSource,
    private val characterDao: CharacterDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    private val personDao: PersonDao,
    private val requestCoordinator: RequestCoordinator
) : DomainCharacterRepository {
    override fun getCharacterDetails(characterId: Long): Flow<CharacterDetails> =
        characterDao.getCharacterDetails(characterId)
            .map { it.asExternalModel() }

    override suspend fun refreshCharacterDetails(characterId: Long, force: Boolean){
        requestCoordinator.sync(
            key = cachedKey(RequestType.Character, characterId),
            forceRefresh = force,
            ttlStrategy = TTLStrategies.CharacterDetails
        ) { updateCharacterDetails(characterId) }
    }

    private suspend fun UpdateScope.updateCharacterDetails(characterId: Long) {
        val response = characterDataSource.getCharacterDetails(characterId)

        val entity = response.asEntity()
        val detailsEntity = response.asDetailsEntity()
        val animeEntities = response.animeEntityShells()
        val mangaEntities = response.mangaEntityShells()
        val personEntities = response.personEntityShells()

        val animeCrossRefs = response.animeCrossRefs()
        val mangaCrossRefs = response.mangaCrossRefs()
        val seyuCrossRef = response.seyuCrossRefs()

        write {
            animeDao.insertOrIgnoreAnimes(animeEntities)
            mangaDao.insertOrIgnoreMangas(mangaEntities)
            personDao.insertOrReplacePersons(personEntities)

            characterDao.insertOrIgnoreCharacter(entity)
            characterDao.insertOrReplaceCharacterDetails(detailsEntity)

            characterDao.insertCharacterAnimeCrossReferences(animeCrossRefs)
            characterDao.insertCharacterMangaCrossReferences(mangaCrossRefs)
            characterDao.insertCharacterSeyuCrossReferences(seyuCrossRef)
        }
    }
}