package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.database.models.character.CharacterAnimeCrossRef
import ru.vladsaybulin.database.models.character.CharacterDetailsEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.CharacterMangaCrossRef
import ru.vladsaybulin.database.models.character.CharacterSeyuCrossRef
import ru.vladsaybulin.database.models.character.PopulatedCharacterDetails

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character_details WHERE id = :characterId")
    fun getCharacterDetails(characterId: Long): Flow<PopulatedCharacterDetails>

    @Insert
    suspend fun insertCharacterSeyuCrossReferences(crossRefs: List<CharacterSeyuCrossRef>)

    @Insert
    suspend fun insertCharacterAnimeCrossReferences(crossRefs: List<CharacterAnimeCrossRef>)

    @Insert
    suspend fun insertCharacterMangaCrossReferences(crossRefs: List<CharacterMangaCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCharacterDetails(characterDetails: CharacterDetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCharacters(characters: List<CharacterEntity>)

    @Query("DELETE FROM character_seyu WHERE character_id = :characterId")
    suspend fun deleteCharacterSeyuCrossRef(characterId: Long)

    @Query("DELETE FROM character_anime WHERE character_id = :characterId")
    suspend fun deleteCharacterAnimeCrossRef(characterId: Long)

    @Query("DELETE FROM character_manga WHERE character_id = :characterId")
    suspend fun deleteCharacterMangaCrossRef(characterId: Long)
}