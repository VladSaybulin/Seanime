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

package ru.vladsaybulin.database.models.manga

import androidx.room.Embedded
import androidx.room.Relation
import ru.vladsaybulin.database.models.person.PersonEntity
import ru.vladsaybulin.database.models.person.asExternalModel
import ru.vladsaybulin.model.person.PersonWithRoles

data class PopulatedMangaAuthor(
    @Embedded val mangaPersonRolesEntity: MangaPersonRolesEntity,

    @Relation(
        entity = PersonEntity::class,
        parentColumn = "person_id",
        entityColumn = "id"
    )
    val person: PersonEntity
)

fun PopulatedMangaAuthor.asExternalModel() = PersonWithRoles(
    person = person.asExternalModel(),
    roles = mangaPersonRolesEntity.roles,
    isMain = mangaPersonRolesEntity.isMain
)