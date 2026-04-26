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

import android.net.Uri
import net.openid.appauth.connectivity.ConnectionBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Creates HttpURLConnection instances with the UserAgent Header
 * using the settings from the default implementation.
 * @see net.openid.appauth.connectivity.DefaultConnectionBuilder
 */
class ShikimoriAuthConnectionBuilder @Inject constructor(
    authInfo: ShikimoriAuthInfo
) : ConnectionBuilder {

    private val userAgent = authInfo.userAgent

    override fun openConnection(uri: Uri): HttpURLConnection {
        val conn = URL(uri.toString()).openConnection() as HttpURLConnection
        conn.setConnectTimeout(CONNECTION_TIMEOUT_MS)
        conn.setReadTimeout(READ_TIMEOUT_MS)
        conn.addRequestProperty(HEADER_USER_AGENT, userAgent)
        conn.instanceFollowRedirects = false
        return conn
    }
}

private val CONNECTION_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(15).toInt()
private val READ_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(10).toInt()