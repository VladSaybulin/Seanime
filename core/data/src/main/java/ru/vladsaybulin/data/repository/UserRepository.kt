package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.vladsaybulin.common.network.di.ApplicationScope
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.datastore.ShikiPreferencesDataSource
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.network.datasource.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val usersDao: UsersDao,
    private val prefsDataSource: ShikiPreferencesDataSource,
    @ApplicationScope appScope: CoroutineScope,
    shikimoriAuthorization: ShikimoriAuthorization
) {
    init {
        appScope.launch {
            shikimoriAuthorization.shikimoriAuthState.drop(1).collect { state ->
                when (state) {
                    ShikimoriAuthState.LOGGED_OUT -> prefsDataSource.setMyId(null)
                    ShikimoriAuthState.LOGGED_IN -> loadAndSaveMe()
                }
            }
        }
    }

    suspend fun getMyId(): Long? =
        prefsDataSource.myId.first()

    private suspend fun loadAndSaveMe() {
        val user = userDataSource.whoAmI()?.asExternalModel()
        checkNotNull(user) { "whoAmI returned null" }
        usersDao.insertOrReplaceUser(user)
        prefsDataSource.setMyId(user.id)
    }

}