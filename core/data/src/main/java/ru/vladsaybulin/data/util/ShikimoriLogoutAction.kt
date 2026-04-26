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

import ru.vladsaybulin.common.auth.LogoutAction
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.database.dao.UserRateDao
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import javax.inject.Inject

class ShikimoriLogoutAction @Inject constructor(
    private val prefsDataSource: SeanimePreferencesDataSource,
    private val userRateDao: UserRateDao,
    private val shikimoriAuthorization: ShikimoriAuthorization
) : LogoutAction {
    override suspend fun logout() {
        shikimoriAuthorization.logout()
        prefsDataSource.setMyId(null)
        userRateDao.deleteAllUserRates()
    }
}