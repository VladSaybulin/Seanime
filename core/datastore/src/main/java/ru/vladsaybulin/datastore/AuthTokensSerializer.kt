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

package ru.vladsaybulin.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import ru.vladsaybulin.core.datastore.proto.AuthTokens
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class AuthTokensSerializer @Inject constructor() : Serializer<AuthTokens> {

    override val defaultValue: AuthTokens = AuthTokens.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AuthTokens =
        try {
            AuthTokens.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read AuthTokens proto.", e)
        }

    override suspend fun writeTo(t: AuthTokens, output: OutputStream) = t.writeTo(output)
}

