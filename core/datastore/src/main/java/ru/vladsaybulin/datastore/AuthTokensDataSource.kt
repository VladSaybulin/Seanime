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

import androidx.datastore.core.DataStore
import com.google.protobuf.ByteString
import kotlinx.coroutines.flow.firstOrNull
import ru.vladsaybulin.core.datastore.proto.AuthTokens
import ru.vladsaybulin.core.datastore.proto.authTokens
import javax.inject.Inject

class AuthTokensDataSource @Inject constructor(
    private val store: DataStore<AuthTokens>
) {
    /** Returns (iv, encryptedBlob, expiresAtMs) or null if not stored. */
    suspend fun getEncrypted(): Triple<ByteArray, ByteArray, Long>? {
        val proto = store.data.firstOrNull() ?: return null
        if (proto.iv.isEmpty || proto.encryptedBlob.isEmpty) return null
        return Triple(
            proto.iv.toByteArray(),
            proto.encryptedBlob.toByteArray(),
            proto.expiresAtMs
        )
    }

    suspend fun saveEncrypted(iv: ByteArray, blob: ByteArray, expiresAtMs: Long) {
        store.updateData {
            authTokens {
                this.iv = ByteString.copyFrom(iv)
                this.encryptedBlob = ByteString.copyFrom(blob)
                this.expiresAtMs = expiresAtMs
            }
        }
    }

    suspend fun clear() {
        store.updateData { AuthTokens.getDefaultInstance() }
    }
}

