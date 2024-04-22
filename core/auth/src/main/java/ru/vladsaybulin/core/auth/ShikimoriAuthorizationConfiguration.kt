package ru.vladsaybulin.core.auth

import android.net.Uri
import net.openid.appauth.AuthorizationServiceConfiguration

internal val ShikimoriAuthorizationServiceConfiguration = AuthorizationServiceConfiguration(
    Uri.parse("${BuildConfig.BASE_URL}/oauth/authorize"),
    Uri.parse("${BuildConfig.BASE_URL}/oauth/token")
)