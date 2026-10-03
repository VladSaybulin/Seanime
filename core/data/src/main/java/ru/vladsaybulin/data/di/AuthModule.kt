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

package ru.vladsaybulin.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.core.auth.OnLogoutCleaner
import ru.vladsaybulin.core.auth.TokenExchangeGateway
import ru.vladsaybulin.core.auth.UserIdFetcher
import ru.vladsaybulin.data.auth.NetworkOAuthTokenExchangeGateway
import ru.vladsaybulin.data.util.SessionUserIdFetcher
import ru.vladsaybulin.data.util.ShikimoriOnLogoutCleaner

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindLogoutAction(logoutAction: ShikimoriOnLogoutCleaner): OnLogoutCleaner

    @Binds
    fun bindUserIdFetcher(fetcher: SessionUserIdFetcher): UserIdFetcher

    @Binds
    fun bindTokenExchangeGateway(gateway: NetworkOAuthTokenExchangeGateway): TokenExchangeGateway
}