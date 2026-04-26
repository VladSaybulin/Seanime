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

package ru.vladsaybulin.core.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.inject.Inject

class GetPagedUserRatesUseCase @Inject constructor(
    private val userRateRepository: UserRateRepository
) {
    operator fun invoke(
        entryType: EntryType,
        userRateStatus: UserRateStatus,
        orderField: UserRateOrderField,
        order: UserRateOrder,
    ): Flow<PagingData<UserRateWithEntry>> =
        when (entryType) {
            EntryType.Anime -> userRateRepository.getPagedAnimeUserRates(userRateStatus, orderField, order)
            EntryType.Manga -> userRateRepository.getPagedMangaUserRates(userRateStatus, orderField, order)
        }
}