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
import ru.vladsaybulin.model.title.TitleType
import ru.vladsaybulin.model.person.PersonWithRoles
import javax.inject.Inject

class GetTitleAuthorsStreamUseCase @Inject constructor(private val repositoryResolver: TitleRepositoryResolver) {
    operator fun invoke(titleType: TitleType, titleId: Long): Flow<ExpandableListWrapper<PersonWithRoles>> =
        repositoryResolver(titleType).getTitleAuthors(titleId)
            .map { authors ->
                val keyAuthors = mutableListOf<PersonWithRoles>()
                val otherAuthors = mutableListOf<PersonWithRoles>()

                authors.forEach { author ->
                    if (author.roles.isKeyRole()) {
                        keyAuthors.add(author)
                    } else {
                        otherAuthors.add(author)
                    }
                }

                val threshold = keyAuthors.size

                ExpandableListWrapper(
                    items = keyAuthors.apply { addAll(otherAuthors) },
                    threshold = threshold
                )
            }

    companion object {
        fun List<String>.isKeyRole(): Boolean = any { it in KEY_ROLES }

        val KEY_ROLES = setOf(
            "Director",
            "Writer",
            "Screenplay",
            "Story",
            "Novel",
            "Manga",
            "Original Creator"
        )
    }
}