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