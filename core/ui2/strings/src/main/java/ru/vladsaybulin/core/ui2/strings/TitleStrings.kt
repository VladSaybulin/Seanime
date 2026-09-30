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

package ru.vladsaybulin.core.ui2.strings

import ru.vladsaybulin.model.title.TitleKind

object TitleStrings {
    fun titleKind(value: TitleKind): Int = when (value) {
        TitleKind.Tv -> R.string.core_ui2_strings_title_kind_tv
        TitleKind.Movie -> R.string.core_ui2_strings_title_kind_movie
        TitleKind.Ona -> R.string.core_ui2_strings_title_kind_ona
        TitleKind.Ova -> R.string.core_ui2_strings_title_kind_ova
        TitleKind.Music -> R.string.core_ui2_strings_title_kind_music
        TitleKind.Special -> R.string.core_ui2_strings_title_kind_special
        TitleKind.TvSpecial -> R.string.core_ui2_strings_title_kind_tv_special
        TitleKind.Pv -> R.string.core_ui2_strings_title_kind_pv
        TitleKind.Cm -> R.string.core_ui2_strings_title_kind_cm
        TitleKind.Manga -> R.string.core_ui2_strings_title_kind_manga
        TitleKind.Manhwa -> R.string.core_ui2_strings_title_kind_manhwa
        TitleKind.Manhua -> R.string.core_ui2_strings_title_kind_manhua
        TitleKind.OneShot -> R.string.core_ui2_strings_title_kind_oneshot
        TitleKind.Doujin -> R.string.core_ui2_strings_title_kind_doujin
        TitleKind.LightNovel -> R.string.core_ui2_strings_title_kind_light_novel
        TitleKind.Novel -> R.string.core_ui2_strings_title_kind_novel
        TitleKind.None -> R.string.core_ui2_strings_none
    }
}