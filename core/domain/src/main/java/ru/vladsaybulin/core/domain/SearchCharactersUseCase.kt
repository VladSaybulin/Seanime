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

package ru.vladsaybulin.core.domain

import ru.vladsaybulin.model.character.CharacterWithRole
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor() {
    operator fun invoke(
        characters: List<CharacterWithRole>,
        searchQuery: String
    ): List<CharacterWithRole> {
        if (searchQuery.isBlank()) return characters

        return characters.filter { characterWithRole ->
            characterWithRole.character.run {
                originalName.contains(searchQuery, ignoreCase = true) ||
                        (russianName != null &&
                                russianName!!.contains(searchQuery, ignoreCase = true))
            }
        }
    }
}
