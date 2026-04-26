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

package ru.vladsaybulin.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
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
    @Transaction
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreCharacter(character: CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceCharacters(characters: List<CharacterEntity>)

}