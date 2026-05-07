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

package ru.vladsaybulin.core.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry

interface UserRateRepository {
    fun getInProgressUserRatesStream(limit: Int): Flow<List<UserRateWithEntry>>

    fun getPagedAnimeUserRates(
        status: UserRateStatus,
        orderField: UserRateOrderField,
        order: UserRateOrder,
        config: PagingConfig = DefaultPagingConfig
    ): Flow<PagingData<UserRateWithEntry>>

    fun getPagedMangaUserRates(
        status: UserRateStatus,
        orderField: UserRateOrderField,
        order: UserRateOrder,
        config: PagingConfig = DefaultPagingConfig
    ): Flow<PagingData<UserRateWithEntry>>

    fun getAnimeUserRateStream(animeId: Long): Flow<UserRate?>

    fun getMangaUserRateStream(mangaId: Long): Flow<UserRate?>

    fun getAllAnimeUserRateStatusesStream(): Flow<Map<Long, UserRateStatus>>

    fun getAllMangaUserRateStatusesStream(): Flow<Map<Long, UserRateStatus>>

    suspend fun createUserRate(entryType: EntryType, entryId: Long, userRateValues: UserRateValues)

    suspend fun updateUserRate(userRateId: Long, userRateValues: UserRateValues)

    suspend fun deleteUserRate(userRateId: Long)

    suspend fun refreshInProgressUserRates()

    companion object {
        val DefaultPagingConfig: PagingConfig
            get() = PagingConfig(
                pageSize = USER_RATES_PAGE_SIZE,
                initialLoadSize = USER_RATES_PAGE_SIZE
            )
    }
}

private const val USER_RATES_PAGE_SIZE = 50

