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

package ru.vladsaybulin.model.search

enum class FilterType(val queryMapKey: QueryMapKey) {
    Kind(QueryMapKey.Kind),
    Status(QueryMapKey.Status),
    MyListStatus(QueryMapKey.MyList),
    Duration(QueryMapKey.Duration),
    Season(QueryMapKey.Season),
    Rating(QueryMapKey.Rating),
    Score(QueryMapKey.Score),
    Genre(QueryMapKey.Genre),
    Theme(QueryMapKey.Genre),
    Demographic(QueryMapKey.Genre),
    Studio(QueryMapKey.Studio),
    Publisher(QueryMapKey.Publisher)
}