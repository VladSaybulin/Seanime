package ru.vladsaybulin.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.user.asExternalModel
import ru.vladsaybulin.datastore.SeanimePreferencesDataSource
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.network.datasource.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val usersDao: UsersDao,
    private val prefsDataSource: SeanimePreferencesDataSource,
    private val shikimoriAuthorization: ShikimoriAuthorization
) {
    suspend fun getMyId(): Long? =
        if (shikimoriAuthorization.shikimoriAuthState.value == ShikimoriAuthState.LOGGED_IN) {
            prefsDataSource.myId.first() ?: updateMeAndReturnMyId()
        } else null

    fun getMeStream(): Flow<BriefUser?> = prefsDataSource.myId.flatMapLatest { myId ->
        if (myId != null) getUserStream(myId) else flowOf(null)
    }

    fun getUserStream(id: Long): Flow<BriefUser> =
        usersDao.getUserById(id).mapNotNull { it?.asExternalModel() }

    private suspend fun updateMeAndReturnMyId(): Long {
        val user = userDataSource.whoAmI()?.asExternalModel()
        checkNotNull(user) { "whoAmI returned null" }
        usersDao.insertOrReplaceUser(user)
        prefsDataSource.setMyId(user.id)
        return user.id
    }
}