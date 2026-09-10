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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.TTLStrategies
import ru.vladsaybulin.data.model.asExternalModel
import ru.vladsaybulin.data.request.RequestCoordinator
import ru.vladsaybulin.data.request.RequestKey
import ru.vladsaybulin.data.request.TTLStrategy
import ru.vladsaybulin.data.withForceStrategy
import ru.vladsaybulin.database.dao.UsersDao
import ru.vladsaybulin.database.models.lastrequest.RequestType
import ru.vladsaybulin.database.models.user.asExternalModel
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.network.datasource.UserDataSource
import javax.inject.Inject
import javax.inject.Singleton
import ru.vladsaybulin.core.domain.repository.UserRepository as DomainUserRepository

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val usersDao: UsersDao,
    private val requestCoordinator: RequestCoordinator,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : DomainUserRepository {

    /**
     * Calls whoAmI on the network, caches the result in the local DB and returns
     * the user. Returns null only when the server returned null (not authenticated).
     * Throws on network errors — callers are responsible for error handling.
     */
    override suspend fun whoAmI(): BriefUser? = withContext(ioDispatcher) {
        val userEntity = userDataSource.whoAmI()?.asExternalModel() // NetworkBriefUser -> UserEntity
            ?: return@withContext null

        requestCoordinator.sync(
            RequestKey.Cached(RequestType.User, userEntity.id),
            TTLStrategies.ForceRefresh
        ) {
            usersDao.insertOrReplaceUser(userEntity)
            userEntity.asExternalModel() // UserEntity -> BriefUser (domain)
        }
    }

    /**
     * Emits the local DB snapshot and refreshes from the network on first subscription.
     */
    override fun getUserStream(id: Long): Flow<BriefUser> =
        usersDao.getUserById(id)
            .map { it.asExternalModel() }
            .onStart { refreshUserBrief(id, true) } //TODO: add force flag

    private suspend fun refreshUserBrief(id: Long, force: Boolean) = withContext(ioDispatcher) {
        requestCoordinator.sync(
            RequestKey.Cached(RequestType.User, id),
            withForceStrategy(force) { TTLStrategies.UserBrief }
        ) {
            val userEntity = userDataSource.getUserBriefById(id).asExternalModel()  // NetworkBriefUser -> UserEntity
            usersDao.insertOrReplaceUser(userEntity)
            userEntity.asExternalModel() // UserEntity -> BriefUser (domain)
        }
    }

    class WhoAmIRefreshPolicy : TTLStrategy {
        private var fetched: Boolean = false

        override fun isExpired(now: kotlinx.datetime.Instant, lastRequest: kotlinx.datetime.Instant): Boolean =
            if (fetched){
                TTLStrategies.UserBrief.isExpired(now, lastRequest)
            } else {
                fetched = true
                true
            }
        }
}