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

package ru.vladsaybulin.core.domain.titledetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.core.domain.common.TitleRepositoryResolver
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.title.TitleType
import javax.inject.Inject

class GetTitleCharactersStreamUseCase @Inject constructor(private val repositoryResolver: TitleRepositoryResolver) {
    operator fun invoke(titleType: TitleType, titleId: Long): Flow<ExpandableListWrapper<CharacterWithRole>> {
        return repositoryResolver(titleType).getTitleCharacters(titleId)
            .map { characters ->
                val mainCharacters = mutableListOf<CharacterWithRole>()
                val supportingCharacters = mutableListOf<CharacterWithRole>()

                characters.forEach { character ->
                    if (character.isMainRole) {
                        mainCharacters.add(character)
                    } else {
                        supportingCharacters.add(character)
                    }
                }

                val threshold = mainCharacters.size

                ExpandableListWrapper(
                    items = mainCharacters.apply { addAll(supportingCharacters) },
                    threshold = threshold
                )
            }
    }
}