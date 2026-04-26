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

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import ru.vladsaybulin.network.common.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApolloModule {
    @Provides
    @Singleton
    fun provideApolloClient(okHttpClient: OkHttpClient) = ApolloClient.Builder()
        .okHttpClient(okHttpClient)
        .serverUrl(SERVER_URL)
        .build()
}

private const val SERVER_URL = BuildConfig.BASE_URL + "/api/graphql"