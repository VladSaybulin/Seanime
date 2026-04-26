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

package ru.vladsaybulin.feature.search

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.GenreKind

sealed class SearchTitle {

    data object Search : SearchTitle()

    data class Status(val entryStatus: EntryStatus) : SearchTitle()

    data class Studio(val studioName: String) : SearchTitle()

    data class Publisher(val publisherName: String) : SearchTitle()

    data class Genre(val genreName: String, val genreKind: GenreKind) : SearchTitle()
}