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

package ru.vladsaybulin.core.ui2.entry.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.Clock
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

class AnimeItemPreviewParameterProvider : PreviewParameterProvider<Anime> {
    override val values: Sequence<Anime> = animes.asSequence()
    override val count: Int = animes.size
}

class ListOfAnimesPreviewParameterProvider : PreviewParameterProvider<List<Anime>> {
    override val values: Sequence<List<Anime>> = sequenceOf(animes)
}

private val animes = listOf(
    Anime(
        id = 17201,
        name = "Toki no Daichi: Hana no Oukoku no Majo",
        russianName = "Земля времени: Ведьма королевства цветов",
        poster = Image("", ""),
        kind = AnimeKind.Ova,
        score = 5.8f,
        status = EntryStatus.Released,
        episodes = 3,
        episodesAired = 0,
        airedOn = IncompleteDate(22, 12, 1998),
        releasedOn = IncompleteDate(25, 3, 1999),
        userRate = UserRate(
            id = 124,
            score = 8,
            status = UserRateStatus.Watching,
            episodes = 2,
            volumes = 0,
            chapters = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            rewatches = 0,
            text = ""
        )
    ),
    Anime(
        id = 35575,
        name = "Nono-chan Theater",
        russianName = "Театр Ноно",
        poster = null,
        kind = AnimeKind.Ona,
        score = 0f,
        status = EntryStatus.None,
        episodes = 85,
        episodesAired = 0,
        airedOn = IncompleteDate(3, 9, 2001),
        releasedOn = IncompleteDate(28, 12, 2001),
        userRate = null
    ),
    Anime(
        id = 42603,
        name = "Boku no Hero Academia: Ikinokore! Kesshi no Survival Kunren",
        russianName = null,
        poster = Image("", ""),
        kind = AnimeKind.None,
        score = 7.16f,
        status = EntryStatus.Ongoing,
        episodes = 5,
        episodesAired = 3,
        airedOn = IncompleteDate(16, 8, 2020),
        releasedOn = null,
        userRate = UserRate(
            id = 124,
            score = 8,
            status = UserRateStatus.Watching,
            episodes = 2,
            volumes = 0,
            chapters = 0,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            rewatches = 0,
            text = ""
        )
    ),
    Anime(
        id = 35921,
        name = "Da Wei Bei Ken: Daomei Tegong Xiong",
        russianName = "Бернард: Агент 008",
        poster= Image("", ""),
        kind = AnimeKind.Movie,
        score = 5.43f,
        status = EntryStatus.Anons,
        episodes = 1,
        episodesAired = 0,
        airedOn = null,
        releasedOn = null,
        userRate = null
    ),
    Anime(
        id = 6823,
        name = "Omocha Bako Series Dai 3 Wa: Ehon 1936-nen",
        russianName="Момотаро против Микки Мауса",
        poster= Image("", ""),
        kind = AnimeKind.None,
        score = 4.63f,
        status = EntryStatus.None,
        episodes = 1,
        episodesAired = 0,
        airedOn = null,
        releasedOn = null,
        userRate = null
    )
)