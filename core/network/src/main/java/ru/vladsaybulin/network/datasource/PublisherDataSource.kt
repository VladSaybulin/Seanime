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
import ru.vladsaybulin.network.models.manga.NetworkPublisher
import javax.inject.Inject

interface PublisherApi {

    @GET("/api/publishers")
    suspend fun getPublishers(): List<NetworkPublisher>
}

class PublisherDataSource @Inject constructor(retrofit: Retrofit) {

    private val api: PublisherApi = retrofit.create()

    suspend fun getPublishers(): List<NetworkPublisher> = api.getPublishers()

}