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

package ru.vladsaybulin.database

internal const val ANIME_PREFIX = "anime_"
internal const val ANIME_ALIAS = "animes"

internal const val ANIME_ROWS = """
    ${ANIME_ALIAS}.id AS ${ANIME_PREFIX}id,
    ${ANIME_ALIAS}.name AS ${ANIME_PREFIX}name,
    ${ANIME_ALIAS}.name_ru AS ${ANIME_PREFIX}name_ru,
    ${ANIME_ALIAS}.poster_original AS ${ANIME_PREFIX}poster_original,
    ${ANIME_ALIAS}.poster_preview AS ${ANIME_PREFIX}poster_preview,
    ${ANIME_ALIAS}.kind AS ${ANIME_PREFIX}kind,
    ${ANIME_ALIAS}.status AS ${ANIME_PREFIX}status,
    ${ANIME_ALIAS}.score AS ${ANIME_PREFIX}score,
    ${ANIME_ALIAS}.episodes AS ${ANIME_PREFIX}episodes,
    ${ANIME_ALIAS}.episodes_aired AS ${ANIME_PREFIX}episodes_aired,
    ${ANIME_ALIAS}.aired_on_day AS ${ANIME_PREFIX}aired_on_day,
    ${ANIME_ALIAS}.aired_on_month AS ${ANIME_PREFIX}aired_on_month,
    ${ANIME_ALIAS}.aired_on_year AS ${ANIME_PREFIX}aired_on_year,
    ${ANIME_ALIAS}.released_on_day AS ${ANIME_PREFIX}released_on_day,
    ${ANIME_ALIAS}.released_on_month AS ${ANIME_PREFIX}released_on_month,
    ${ANIME_ALIAS}.released_on_year AS ${ANIME_PREFIX}released_on_year
"""

internal const val MANGA_PREFIX = "manga_"
internal const val MANGA_ALIAS = "mangas"

internal const val MANGA_ROWS = """
    ${MANGA_ALIAS}.id AS ${MANGA_PREFIX}id,
    ${MANGA_ALIAS}.name AS ${MANGA_PREFIX}name,
    ${MANGA_ALIAS}.name_ru AS ${MANGA_PREFIX}name_ru,
    ${MANGA_ALIAS}.poster_original AS ${MANGA_PREFIX}poster_original,
    ${MANGA_ALIAS}.poster_preview AS ${MANGA_PREFIX}poster_preview,
    ${MANGA_ALIAS}.kind AS ${MANGA_PREFIX}kind,
    ${MANGA_ALIAS}.status AS ${MANGA_PREFIX}status,
    ${MANGA_ALIAS}.score AS ${MANGA_PREFIX}score,
    ${MANGA_ALIAS}.volumes AS ${MANGA_PREFIX}volumes,
    ${MANGA_ALIAS}.chapters AS ${MANGA_PREFIX}chapters,
    ${MANGA_ALIAS}.aired_on_day AS ${MANGA_PREFIX}aired_on_day,
    ${MANGA_ALIAS}.aired_on_month AS ${MANGA_PREFIX}aired_on_month,
    ${MANGA_ALIAS}.aired_on_year AS ${MANGA_PREFIX}aired_on_year,
    ${MANGA_ALIAS}.released_on_day AS ${MANGA_PREFIX}released_on_day,
    ${MANGA_ALIAS}.released_on_month AS ${MANGA_PREFIX}released_on_month,
    ${MANGA_ALIAS}.released_on_year AS ${MANGA_PREFIX}released_on_year
"""
internal const val CHARACTER_PREFIX = "character_"
internal const val CHARACTER_ALIAS = "characters"
internal const val CHARACTER_ROWS = """
    ${CHARACTER_ALIAS}.id AS ${CHARACTER_PREFIX}id,
    ${CHARACTER_ALIAS}.name AS ${CHARACTER_PREFIX}name,
    ${CHARACTER_ALIAS}.name_ru AS ${CHARACTER_PREFIX}name_ru,
    ${CHARACTER_ALIAS}.image_original AS ${CHARACTER_PREFIX}image_original,
    ${CHARACTER_ALIAS}.image_preview AS ${CHARACTER_PREFIX}image_preview
"""

internal const val PERSON_PREFIX = "person_"
internal const val PERSON_ALIAS = "persons"

internal const val PERSON_ROWS = """
    ${PERSON_ALIAS}.id AS ${PERSON_PREFIX}id,
    ${PERSON_ALIAS}.name AS ${PERSON_PREFIX}name,
    ${PERSON_ALIAS}.name_ru AS ${PERSON_PREFIX}name_ru,
    ${PERSON_ALIAS}.image_original AS ${PERSON_PREFIX}image_original,
    ${PERSON_ALIAS}.image_preview AS ${PERSON_PREFIX}image_preview
"""