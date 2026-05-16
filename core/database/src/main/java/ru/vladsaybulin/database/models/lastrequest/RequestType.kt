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

package ru.vladsaybulin.database.models.lastrequest

enum class RequestType(val serializedValue: String) {
    Anime("anime"),
    AnimeRoles("anime_roles"),
    SimilarAnimes("similar_animes"),
    Manga("manga"),
    MangaRoles("manga_roles"),
    SimilarMangas("similar_mangas"),
    Character("character"),
    OngoingAnimes("ongoing_animes"),
    News("news"),
    Calendar("calendar"),
    InProgressRates("in_progress_rates"),
}