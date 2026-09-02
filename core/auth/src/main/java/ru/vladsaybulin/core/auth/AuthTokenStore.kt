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

package ru.vladsaybulin.core.auth

import ru.vladsaybulin.datastore.AuthTokensDataSource
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Encrypts tokens with AES-256-GCM using an Android KeyStore key,
 * then persists the ciphertext in a DataStore proto file.
 *
 * Layout of the encrypted blob: "accessToken\nrefreshToken"
 * (tokens are Base64url so \n never appears inside them).
 *
 * ## In-memory cache
 * Decrypted tokens are kept in [cachedTokens] for the process lifetime so that
 * the KeyStore cipher is invoked only once per session (on first read or after a
 * write). The cache is invalidated immediately on [clearTokens].
 *
 * Thread-safety: [cachedTokens] is `@Volatile`. If two coroutines race on a
 * cache-miss they will both decrypt — that is intentional: the result is
 * identical and idempotent, so the last write wins harmlessly.
 */
@Singleton
class AuthTokenStore @Inject constructor(
    private val dataSource: AuthTokensDataSource
) {
    private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }

    @Volatile private var cachedTokens: StoredTokens? = null

    suspend fun saveTokens(tokens: StoredTokens) {
        val plain = "${tokens.accessToken}\n${tokens.refreshToken}"
        val cipher = encryptCipher()
        val iv = cipher.iv
        val blob = cipher.doFinal(plain.toByteArray(Charsets.UTF_8))
        dataSource.saveEncrypted(iv, blob, tokens.expiresAtMs)
        cachedTokens = tokens   // keep cache warm
    }

    /**
     * Fast-path: returns the in-memory cache if available.
     * Cold-path: decrypts once from DataStore, then caches the result.
     * Returns null if no tokens are stored or if decryption fails.
     */
    suspend fun getTokens(): StoredTokens? {
        cachedTokens?.let { return it }  // fast path — no crypto needed

        val (iv, blob, expiresAtMs) = dataSource.getEncrypted() ?: return null
        return runCatching {
            val plain = decryptCipher(iv).doFinal(blob).toString(Charsets.UTF_8)
            val parts = plain.split("\n", limit = 2)
            StoredTokens(
                accessToken = parts[0],
                refreshToken = parts[1],
                expiresAtMs = expiresAtMs
            )
        }.getOrNull()   // Treat decryption failure as missing tokens
            ?.also { cachedTokens = it }
    }

    suspend fun getRefreshToken(): String? = getTokens()?.refreshToken

    suspend fun clearTokens() {
        cachedTokens = null     // invalidate cache before touching storage
        dataSource.clear()
    }

    private fun getOrCreateKey(): SecretKey {
        keyStore.getKey(KEY_ALIAS, null)?.let { return it as SecretKey }
        val generator = KeyGenerator.getInstance(
            android.security.keystore.KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEY_STORE
        )
        generator.init(
            android.security.keystore.KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or
                        android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256)
                .build()
        )
        return generator.generateKey()
    }

    private fun encryptCipher(): Cipher =
        Cipher.getInstance(AES_GCM_NO_PADDING).also {
            it.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        }

    private fun decryptCipher(iv: ByteArray): Cipher =
        Cipher.getInstance(AES_GCM_NO_PADDING).also {
            it.init(Cipher.DECRYPT_MODE, getOrCreateKey(), GCMParameterSpec(GCM_TAG_BITS, iv))
        }

    private companion object {
        const val KEY_ALIAS = "seanime_auth_key"
        const val ANDROID_KEY_STORE = "AndroidKeyStore"
        const val AES_GCM_NO_PADDING = "AES/GCM/NoPadding"
        const val GCM_TAG_BITS = 128
    }
}


