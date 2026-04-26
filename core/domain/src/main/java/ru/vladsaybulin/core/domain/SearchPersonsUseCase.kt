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

import kotlinx.coroutines.CoroutineDispatcher
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.Default
import ru.vladsaybulin.model.person.PersonWithRoles
import javax.inject.Inject

class SearchPersonsUseCase @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        authors: List<PersonWithRoles>,
        searchQuery: String
    ): List<PersonWithRoles> {
        if (searchQuery.isBlank()) return authors
        return authors.filter {
            with(it.person) {
                originalName.contains(searchQuery, ignoreCase = true) ||
                        (russianName != null &&
                                russianName!!.contains(searchQuery, ignoreCase = true))
            }
        }
    }
}