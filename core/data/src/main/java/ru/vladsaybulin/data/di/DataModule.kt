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

package ru.vladsaybulin.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.common.auth.LogoutAction
import ru.vladsaybulin.core.domain.repository.AnimeRepository
import ru.vladsaybulin.core.domain.repository.CalendarRepository
import ru.vladsaybulin.core.domain.repository.CharacterRepository
import ru.vladsaybulin.core.domain.repository.FilterGenreRepository
import ru.vladsaybulin.core.domain.repository.FilterPublisherRepository
import ru.vladsaybulin.core.domain.repository.FilterStudioRepository
import ru.vladsaybulin.core.domain.repository.FiltersRepository
import ru.vladsaybulin.core.domain.repository.LastRequestRepository
import ru.vladsaybulin.core.domain.repository.MangaRepository
import ru.vladsaybulin.core.domain.repository.TopicsRepository
import ru.vladsaybulin.core.domain.repository.UserRateRepository
import ru.vladsaybulin.core.domain.repository.UserRepository
import ru.vladsaybulin.data.repository.AnimeRepository as DataAnimeRepository
import ru.vladsaybulin.data.repository.CalendarRepository as DataCalendarRepository
import ru.vladsaybulin.data.repository.CharacterRepository as DataCharacterRepository
import ru.vladsaybulin.data.repository.FilterGenreRepository as DataFilterGenreRepository
import ru.vladsaybulin.data.repository.FilterPublisherRepository as DataFilterPublisherRepository
import ru.vladsaybulin.data.repository.FilterStudioRepository as DataFilterStudioRepository
import ru.vladsaybulin.data.repository.FiltersRepository as DataFiltersRepository
import ru.vladsaybulin.data.repository.LastRequestRepository as DataLastRequestRepository
import ru.vladsaybulin.data.repository.MangaRepository as DataMangaRepository
import ru.vladsaybulin.data.repository.TopicsRepository as DataTopicsRepository
import ru.vladsaybulin.data.repository.UserRateRepository as DataUserRateRepository
import ru.vladsaybulin.data.repository.UserRepository as DataUserRepository
import ru.vladsaybulin.data.util.ShikimoriLogoutAction

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindLogoutAction(logoutAction: ShikimoriLogoutAction): LogoutAction

    @Binds
    fun bindAnimeRepository(repository: DataAnimeRepository): AnimeRepository

    @Binds
    fun bindCalendarRepository(repository: DataCalendarRepository): CalendarRepository

    @Binds
    fun bindCharacterRepository(repository: DataCharacterRepository): CharacterRepository

    @Binds
    fun bindFilterGenreRepository(repository: DataFilterGenreRepository): FilterGenreRepository

    @Binds
    fun bindFilterPublisherRepository(repository: DataFilterPublisherRepository): FilterPublisherRepository

    @Binds
    fun bindFilterStudioRepository(repository: DataFilterStudioRepository): FilterStudioRepository

    @Binds
    fun bindFiltersRepository(repository: DataFiltersRepository): FiltersRepository

    @Binds
    fun bindLastRequestRepository(repository: DataLastRequestRepository): LastRequestRepository

    @Binds
    fun bindMangaRepository(repository: DataMangaRepository): MangaRepository

    @Binds
    fun bindTopicsRepository(repository: DataTopicsRepository): TopicsRepository

    @Binds
    fun bindUserRateRepository(repository: DataUserRateRepository): UserRateRepository

    @Binds
    fun bindUserRepository(repository: DataUserRepository): UserRepository
}