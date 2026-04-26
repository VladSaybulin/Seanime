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

package ru.vladsaybulin.feature.home

import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.topic.Topic
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.annotation.concurrent.Immutable

@Immutable
sealed class HomeUiState {
    data object Loading : HomeUiState()

    @Immutable
    data class Error(val throwable: Throwable) : HomeUiState()

    @Immutable
    data class Success(
        val inProgressUserRates: ImmutableList<UserRateWithEntry>,
        val ongoings: ImmutableList<Anime>,
        val newsTopics: ImmutableList<Topic>,
        val me: BriefUser?
    ) : HomeUiState()
}