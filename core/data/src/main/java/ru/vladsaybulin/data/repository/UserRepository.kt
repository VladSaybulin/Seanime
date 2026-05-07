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

package ru.vladsaybulin.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.core.domain.repository.UserRepository as DomainUserRepository
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
) : DomainUserRepository {
    override suspend fun getMyId(): Long? = getMyIdStream().first()

    override fun getMyIdStream(): Flow<Long?> = shikimoriAuthorization.shikimoriAuthState.map {
        if (it == ShikimoriAuthState.LOGGED_IN) requireMyId() else null
    }

    override fun getMeStream(): Flow<BriefUser?> = getMyIdStream()
        .flatMapLatest { myId ->
            myId?.let { nonNullMyId ->
                usersDao.getUserById(nonNullMyId).map { it.asExternalModel() }
            } ?: flowOf(null)
        }

    override fun getUserStream(id: Long): Flow<BriefUser> =
        usersDao.getUserById(id).mapNotNull { it.asExternalModel() }
            .onStart { refreshUserBrief(id) }

    private suspend fun refreshUserBrief(id: Long) {
        withContext(ioDispatcher) {
            val response = userDataSource.getUserBriefById(id)

            usersDao.insertOrReplaceUser(response.asExternalModel())
        }
    }

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