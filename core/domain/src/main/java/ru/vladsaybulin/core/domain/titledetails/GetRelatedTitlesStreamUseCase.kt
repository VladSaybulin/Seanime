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
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.related.RelationType
import javax.inject.Inject

class GetRelatedTitlesStreamUseCase @Inject constructor(private val repositoryResolver: TitleRepositoryResolver) {
    operator fun invoke(titleType: TitleType, titleId: Long): Flow<ExpandableListWrapper<RelatedTitle>> =
        repositoryResolver(titleType).getRelatedTitles(titleId)
            .map { titles ->
                val sequels = mutableListOf<RelatedTitle>()
                val prequels = mutableListOf<RelatedTitle>()
                val adaptations = mutableListOf<RelatedTitle>()
                val other = mutableListOf<RelatedTitle>()

                titles.forEach { title ->
                    when (title.relationType) {
                        RelationType.Sequel -> sequels.add(title)
                        RelationType.Prequel -> prequels.add(title)
                        RelationType.Adaptation -> adaptations.add(title)
                        else -> other.add(title)
                    }
                }
                val threshold = (sequels.size + prequels.size + adaptations.size).coerceAtMost(MAX_VISIBLE_ITEMS)

                ExpandableListWrapper(
                    items = sequels.apply {
                        addAll(prequels)
                        addAll(adaptations)
                        addAll(other)
                    },
                     threshold = threshold
                )
            }
}

private const val MAX_VISIBLE_ITEMS = 5