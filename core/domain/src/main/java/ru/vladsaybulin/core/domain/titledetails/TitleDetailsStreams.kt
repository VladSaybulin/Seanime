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
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.title.TitleDetails
import ru.vladsaybulin.model.title.TitleRoles
import ru.vladsaybulin.model.userrate.UserRate

class TitleDetailsStreams(
    val brief: Flow<Title>,
    val details: Flow<TitleDetails>,
    val roles: Flow<TitleRoles>,
    val similar: Flow<List<Title>>,
    val userRate: Flow<UserRate?>,
    val errors: Flow<Throwable>
)