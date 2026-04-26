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

package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import ru.vladsaybulin.network.models.user.NetworkBriefUser
import javax.inject.Inject

interface UsersApi {

    @GET("/api/users/whoami")
    suspend fun whoAmI(): NetworkBriefUser?

    @GET("/api/users/{id}/info")
    suspend fun getUserBriefById(@Path("id") id: Long): NetworkBriefUser

}

class UserDataSource @Inject constructor(
    retrofit: Retrofit
) {

    private val api = retrofit.create<UsersApi>()

    suspend fun whoAmI(): NetworkBriefUser? = api.whoAmI()

    suspend fun getUserBriefById(id: Long): NetworkBriefUser = api.getUserBriefById(id)
}