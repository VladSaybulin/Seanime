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

package ru.vladsaybulin.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.vladsaybulin.network.util.ShikimoriAuthenticator
import ru.vladsaybulin.network.util.interceptors.AuthorizationInterceptor
import ru.vladsaybulin.network.util.interceptors.UserAgentInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ClientModule {

    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        userAgentInterceptor: UserAgentInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        authorizationInterceptor: AuthorizationInterceptor,
        authenticator: ShikimoriAuthenticator
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(userAgentInterceptor)
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(authenticator)
            .build()
}