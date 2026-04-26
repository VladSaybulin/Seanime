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

package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.AnimeStatusEnum
import ru.vladsaybulin.core.network.graphql.type.MangaStatusEnum
import ru.vladsaybulin.model.common.EntryStatus

fun AnimeStatusEnum?.asEntryStatus() = when (this) {
    AnimeStatusEnum.anons -> EntryStatus.Anons
    AnimeStatusEnum.ongoing -> EntryStatus.Ongoing
    AnimeStatusEnum.released -> EntryStatus.Released
    else -> EntryStatus.None
}

fun MangaStatusEnum?.asEntryStatus() = when (this) {
    MangaStatusEnum.anons -> EntryStatus.Anons
    MangaStatusEnum.ongoing -> EntryStatus.Ongoing
    MangaStatusEnum.released -> EntryStatus.Released
    MangaStatusEnum.discontinued -> EntryStatus.Discontinued
    MangaStatusEnum.paused -> EntryStatus.Paused
    else -> EntryStatus.None
}