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

import ru.vladsaybulin.core.auth.BuildConfig
import ru.vladsaybulin.database.models.anime.StudioEntity
import ru.vladsaybulin.database.models.filters.FilterStudioEntity
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.network.models.anime.NetworkStudio

fun NetworkStudio.asFilterEntity() = FilterStudioEntity(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)

fun NetworkStudio.asEntity() = StudioEntity(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)

fun NetworkStudio.asExternalModel() = Studio(
    id = id,
    name = name,
    imageUrl = "${BuildConfig.BASE_URL}$image"
)