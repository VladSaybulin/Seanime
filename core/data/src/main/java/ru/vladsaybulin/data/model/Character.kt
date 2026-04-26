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

import ru.vladsaybulin.database.models.character.CharacterAnimeCrossRef
import ru.vladsaybulin.database.models.character.CharacterDetailsEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.CharacterMangaCrossRef
import ru.vladsaybulin.database.models.character.CharacterSeyuCrossRef
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.network.models.character.NetworkCharacter
import ru.vladsaybulin.network.models.character.NetworkCharacterDetails
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole

fun NetworkCharacterWithRole.asExternalModel() = CharacterWithRole(
    character = character.asExternalModel(),
    isMain = isMain
)

fun NetworkCharacter.asExternalModel() = Character(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)

fun NetworkCharacter.asEntity() = CharacterEntity(
    id = id,
    name = name,
    nameRu = nameRu,
    image = image?.asPOJO()
)

fun NetworkCharacterDetails.asEntity() = CharacterEntity(
    id = id,
    name = name,
    nameRu = nameRu,
    image = image?.asPOJO()
)

fun NetworkCharacterDetails.asDetailsEntity() = CharacterDetailsEntity(
    id = id,
    nameJp = nameJp,
    altNames = alternativeName,
    description = descriptionHtml?.asSeanimeText()?.asSeanimeTextPOJO(),
    descriptionSource = descriptionSource,
    topicId = topicId,
    updatedAt = updatedAt
)

fun NetworkCharacterDetails.personEntityShells() = seyu.map { it.asEntity() }

fun NetworkCharacterDetails.seyuCrossRefs() = seyu.map {
    CharacterSeyuCrossRef(id, it.id)
}

fun NetworkCharacterDetails.animeEntityShells() = animes.map { it.asEntity() }

fun NetworkCharacterDetails.animeCrossRefs() = animes.map {
    CharacterAnimeCrossRef(id, it.id)
}

fun NetworkCharacterDetails.mangaEntityShells() = mangas.map { it.asEntity() }

fun NetworkCharacterDetails.mangaCrossRefs() = mangas.map {
    CharacterMangaCrossRef(id, it.id)
}