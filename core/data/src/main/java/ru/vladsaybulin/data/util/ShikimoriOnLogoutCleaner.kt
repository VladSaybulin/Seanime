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

package ru.vladsaybulin.data.util

import ru.vladsaybulin.core.auth.OnLogoutCleaner
import ru.vladsaybulin.database.dao.UserRateDao
import javax.inject.Inject

/**
 * Local cleanup performed on logout: clears cached user-rates from DB.
 *
 * Token clearing and state reset are handled by [SessionManager] before
 * calling this action, so no auth references are needed here.
 */
class ShikimoriOnLogoutCleaner @Inject constructor(
    private val userRateDao: UserRateDao
) : OnLogoutCleaner {
    override suspend fun onLogout() {
        userRateDao.deleteAllUserRates()
    }
}