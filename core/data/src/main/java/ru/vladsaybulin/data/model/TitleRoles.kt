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

package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.asExternalModel
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.person.asExternalModel
import ru.vladsaybulin.database.models.title.PopulatedCharacterWithRole
import ru.vladsaybulin.database.models.title.PopulatedPersonWithRoles
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.network.models.common.NetworkTitleRoles

// ==========================================
// Network -> Database
// ==========================================

internal inline fun <RE> NetworkTitleRoles.extractCharacters(
    roleEntities: MutableList<RE>,
    characterEntities: MutableList<CharacterEntity>,
    crossinline roleEntityFactory: (characterId: Long, isMain: Boolean) -> RE
) {
    characters?.forEach { characterWithRole ->
        val characterEntity = characterWithRole.character.asEntity()
        characterEntities.add(characterEntity)
        roleEntities.add(roleEntityFactory(characterEntity.id, characterWithRole.isMain))
    }
}

internal inline fun <RE> NetworkTitleRoles.extractPersons(
    roleEntities: MutableList<RE>,
    personEntities: MutableList<PersonEntity>,
    crossinline roleEntityFactory: (personId: Long, roles: List<String>) -> RE
) {
    authors?.forEach { personWithRoles ->
        val personEntity = personWithRoles.person.asEntity()
        personEntities.add(personEntity)
        roleEntities.add(
            roleEntityFactory(
                personEntity.id,
                personWithRoles.roles
            )
        )
    }
}

// ==========================================
// Database -> Domain
// ==========================================

fun PopulatedCharacterWithRole.asExternalModel() = CharacterWithRole(
    character = character.asExternalModel(),
    isMainRole = isMainRole
)

fun PopulatedPersonWithRoles.asExternalModel() = PersonWithRoles(
    person = person.asExternalModel(),
    roles = roles
)