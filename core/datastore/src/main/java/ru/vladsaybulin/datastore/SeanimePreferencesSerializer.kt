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
import ru.vladsaybulin.core.datastore.proto.SeanimePreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class SeanimePreferencesSerializer @Inject constructor() : Serializer<SeanimePreferences> {

    override val defaultValue: SeanimePreferences = SeanimePreferences.getDefaultInstance().toBuilder()
        .setMyId(NULL_MY_ID)
        .build()

    override suspend fun readFrom(input: InputStream): SeanimePreferences =
        try {
            // readFrom is already called on the data store background thread
            SeanimePreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: SeanimePreferences, output: OutputStream) {
        // writeTo is already called on the data store background thread
        t.writeTo(output)
    }

}