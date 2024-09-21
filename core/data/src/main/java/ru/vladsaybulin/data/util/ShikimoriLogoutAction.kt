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