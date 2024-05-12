package ru.vladsaybulin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.datetime.Clock
import ru.vladsaybulin.data.model.animeCrossRefs
import ru.vladsaybulin.data.model.animeEntityShells
import ru.vladsaybulin.data.model.asDetailsEntity
import ru.vladsaybulin.data.model.asEntity
import ru.vladsaybulin.data.model.mangaCrossRefs
import ru.vladsaybulin.data.model.mangaEntityShells
import ru.vladsaybulin.data.model.personEntityShells
import ru.vladsaybulin.data.model.seyuCrossRefs
import ru.vladsaybulin.data.util.sync
import ru.vladsaybulin.database.DatabaseTransactionRunner
import ru.vladsaybulin.database.dao.AnimeDao
import ru.vladsaybulin.database.dao.CharacterDao
import ru.vladsaybulin.database.dao.LastRequestDao
import ru.vladsaybulin.database.dao.MangaDao
import ru.vladsaybulin.database.dao.PersonDao
import ru.vladsaybulin.database.models.character.asExternalModel
import ru.vladsaybulin.database.models.lastrequest.LastCharacterDetailsRequestEntity
import ru.vladsaybulin.model.character.CharacterDetails
import ru.vladsaybulin.network.datasource.CharacterDataSource
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

class CharacterRepository @Inject constructor(
    private val characterDataSource: CharacterDataSource,
    private val characterDao: CharacterDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    private val personDao: PersonDao,
    private val lastRequestDao: LastRequestDao,
    private val databaseTransactionRunner: DatabaseTransactionRunner
) {
    fun getCharacterDetails(characterId: Long): Flow<CharacterDetails> =
        characterDao.getCharacterDetails(characterId)
            .onStart { syncCharacterDetails(characterId) }
            .map { it.asExternalModel() }

    suspend fun refreshCharacterDetails(characterId: Long) {
        val response = characterDataSource.getCharacterDetails(characterId)

        val entity = response.asEntity()
        val detailsEntity = response.asDetailsEntity()
        val animeEntities = response.animeEntityShells()
        val mangaEntities = response.mangaEntityShells()
        val personEntities = response.personEntityShells()

        val animeCrossRefs = response.animeCrossRefs()
        val mangaCrossRefs = response.mangaCrossRefs()
        val seyuCrossRef = response.seyuCrossRefs()

        databaseTransactionRunner {
            animeDao.insertOrReplaceAnimes(animeEntities)
            mangaDao.insertOrReplaceMangas(mangaEntities)
            personDao.insertOrReplacePersons(personEntities)

            characterDao.insertOrReplaceCharacter(entity)
            characterDao.insertOrReplaceCharacterDetails(detailsEntity)

            characterDao.deleteCharacterAnimeCrossRef(characterId)
            characterDao.deleteCharacterMangaCrossRef(characterId)
            characterDao.deleteCharacterSeyuCrossRef(characterId)

            characterDao.insertCharacterAnimeCrossReferences(animeCrossRefs)
            characterDao.insertCharacterMangaCrossReferences(mangaCrossRefs)
            characterDao.insertCharacterSeyuCrossReferences(seyuCrossRef)

            lastRequestDao.insertOrReplaceLastCharacterDetailsRequest(
                LastCharacterDetailsRequestEntity(
                    characterId = characterId,
                    lastRequestDate = Clock.System.now()
                )
            )
        }
    }

    private suspend fun syncCharacterDetails(characterId: Long) {
        sync(
            param = characterId,
            ttl = 1.minutes,
            readLastUpdateDate = lastRequestDao::getLastCharacterDetailsRequestDate,
            refresh = ::refreshCharacterDetails
        )
    }
}