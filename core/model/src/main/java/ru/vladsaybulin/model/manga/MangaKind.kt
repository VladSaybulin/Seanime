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

package ru.vladsaybulin.model.manga

enum class MangaKind(val serializedName: String) {
    Manga("manga"),
    Manhwa("manhwa"),
    Manhua("manhua"),
    OneShot("one_shot"),
    Doujin("doujin"),

    //Ranobe kind
    LightNovel("light_novel"),
    Novel("novel"),

    None("")
}

val mangaKind = listOf(
    MangaKind.Manga,
    MangaKind.Manhwa,
    MangaKind.Manhua,
    MangaKind.OneShot,
    MangaKind.Doujin
)

val ranobeKind = listOf(
    MangaKind.LightNovel,
    MangaKind.Novel
)

fun String?.asMangaKind() = when (this) {
    null -> MangaKind.None
    else -> MangaKind.entries.firstOrNull { it.serializedName == this } ?: MangaKind.None
}
