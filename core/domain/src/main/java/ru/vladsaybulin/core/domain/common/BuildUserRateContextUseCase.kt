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

package ru.vladsaybulin.core.domain.common

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.userrate.UserRateContext
import javax.inject.Inject

class BuildUserRateContextUseCase @Inject constructor() {
    operator fun invoke(brief: Title): UserRateContext = when (brief.type) {
        EntryType.Anime -> buildAnimeRateContext(
            status = brief.status,
            episodes = brief.episodes,
            episodesAired = brief.episodesAired
        )

        EntryType.Manga -> buildMangaRateContext(
            status = brief.status,
            chapters = brief.chapters,
            volumes = brief.volumes
        )
    }

    private fun buildAnimeRateContext(
        status: EntryStatus,
        episodes: Int,
        episodesAired: Int,
    ): UserRateContext =
        UserRateContext(
            titleStatus = status,
            maxEpisodes = when {
                status.isFinished() -> maxOf(episodes, episodesAired)
                status == EntryStatus.Anons -> -1
                episodes > episodesAired -> episodes
                else -> episodesAired
            },
            maxChapters = -1,
            maxVolumes = -1

        )

    private fun buildMangaRateContext(
        status: EntryStatus,
        chapters: Int,
        volumes: Int
    ): UserRateContext = UserRateContext(
        titleStatus = status,
        maxEpisodes = -1,
        maxChapters = when {
            status.isFinished() && chapters > 0 -> chapters
            chapters == 0 -> -1
            else -> Int.MAX_VALUE
        },
        maxVolumes = when {
            status.isFinished() && volumes > 0 -> volumes
            volumes == 0 -> -1
            else -> Int.MAX_VALUE
        }
    )
}

private fun EntryStatus.isFinished(): Boolean =
    this == EntryStatus.Released || this == EntryStatus.Discontinued || this == EntryStatus.Paused