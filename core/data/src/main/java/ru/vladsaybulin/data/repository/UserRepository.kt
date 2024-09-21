package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
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
    private val shikimoriAuthorization: ShikimoriAuthorization,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getMyId(): Long? = getMyIdStream().first()

    private fun getMyIdStream(): Flow<Long?> = shikimoriAuthorization.shikimoriAuthState.map {
        if (it == ShikimoriAuthState.LOGGED_IN) requireMyId() else null
    }

    fun getMeStream(): Flow<BriefUser?> = getMyIdStream()
        .flatMapLatest { myId ->
            myId?.let { nonNullMyId ->
                usersDao.getUserById(nonNullMyId).map { it.asExternalModel() }
            } ?: flowOf(null)
        }

    fun getUserStream(id: Long): Flow<BriefUser> =
        usersDao.getUserById(id).mapNotNull { it.asExternalModel() }

    private suspend fun requireMyId(): Long =
        prefsDataSource.myId.first() ?: updateMeAndReturnMyId()

    private suspend fun updateMeAndReturnMyId(): Long = withContext(ioDispatcher) {
        val user = userDataSource.whoAmI()?.asExternalModel()
        checkNotNull(user) { "whoAmI returned null" }
        usersDao.insertOrReplaceUser(user)
        prefsDataSource.setMyId(user.id)
        user.id
    }
}