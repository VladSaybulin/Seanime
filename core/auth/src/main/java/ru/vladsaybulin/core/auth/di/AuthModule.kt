package ru.vladsaybulin.core.auth.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ClientAuthentication
import net.openid.appauth.ClientSecretPost
import net.openid.appauth.connectivity.ConnectionBuilder
import ru.vladsaybulin.core.auth.BuildConfig
import ru.vladsaybulin.core.auth.ShikimoriAuthConnectionBuilder
import ru.vladsaybulin.core.auth.ShikimoriAuthInfo

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    fun provideShikimoriAuthInfo() = ShikimoriAuthInfo(
        clientId = BuildConfig.SHIKIMORI_CLIENT_ID,
        clientSecret = BuildConfig.SHIKIMORI_CLIENT_SECRET,
        scope = "user_rates comments topics",
        redirectUri = BuildConfig.SHIKIMORI_AUTH_REDIRECT_URI,
        userAgent = BuildConfig.SHIKIMORI_USER_AGENT
    )

    @Provides
    fun provideAuthConfig() = AuthorizationServiceConfiguration(
        Uri.parse("${BuildConfig.BASE_URL}/oauth/authorize"),
        Uri.parse("${BuildConfig.BASE_URL}/oauth/token")
    )

    @Provides
    fun provideClientAuthentication(authInfo: ShikimoriAuthInfo): ClientAuthentication =
        ClientSecretPost(authInfo.clientSecret)

    @Provides
    fun provideAuthConnectionBuilder(info: ShikimoriAuthInfo): ConnectionBuilder =
        ShikimoriAuthConnectionBuilder(info)

    @Provides
    fun provideAuthService(@ApplicationContext context: Context) =
        AuthorizationService(context)

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("shikimori_auth", MODE_PRIVATE)

}