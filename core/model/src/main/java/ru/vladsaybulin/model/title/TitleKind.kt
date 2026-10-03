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

package ru.vladsaybulin.model.title

import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.manga.MangaKind

enum class TitleKind(val serializedValue: String) {

    // Anime

    Tv("tv"),
    Movie("movie"),
    Ona("ona"),
    Ova("ova"),
    Music("music"),
    Special("special"),
    TvSpecial("tv_special"),
    Pv("rv"),
    Cm("cm"),

    // Manga

    Manga("manga"),
    Manhwa("manhwa"),
    Manhua("manhua"),
    OneShot("one_shot"),
    Doujin("doujin"),

    //Ranobe

    LightNovel("light_novel"),
    Novel("novel"),

    None("")
}

val AnimeKindList = listOf(
    TitleKind.Tv,
    TitleKind.Movie,
    TitleKind.Ona,
    TitleKind.Ova,
    TitleKind.Music,
    TitleKind.Special,
    TitleKind.TvSpecial,
    TitleKind.Pv,
    TitleKind.Cm
)

val MangaKindList = listOf(
    TitleKind.Manga,
    TitleKind.Manhwa,
    TitleKind.Manhua,
    TitleKind.OneShot,
    TitleKind.Doujin
)

val RanobeKindList = listOf(
    TitleKind.LightNovel,
    TitleKind.Novel
)

fun AnimeKind.toTitleKind() = when (this) {
    AnimeKind.Tv -> TitleKind.Tv
    AnimeKind.Movie -> TitleKind.Movie
    AnimeKind.Ona -> TitleKind.Ona
    AnimeKind.Ova -> TitleKind.Ova
    AnimeKind.Music -> TitleKind.Music
    AnimeKind.Special -> TitleKind.Special
    AnimeKind.TvSpecial -> TitleKind.TvSpecial
    AnimeKind.Pv -> TitleKind.Pv
    AnimeKind.Cm -> TitleKind.Cm
    AnimeKind.None -> TitleKind.None
}

fun MangaKind.toTitleKind() = when (this) {
    MangaKind.Manga -> TitleKind.Manga
    MangaKind.Manhwa -> TitleKind.Manhwa
    MangaKind.Manhua -> TitleKind.Manhua
    MangaKind.OneShot -> TitleKind.OneShot
    MangaKind.Doujin -> TitleKind.Doujin
    MangaKind.LightNovel -> TitleKind.LightNovel
    MangaKind.Novel -> TitleKind.Novel
    MangaKind.None -> TitleKind.None
}