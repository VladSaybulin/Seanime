package ru.vladsaybulin.core.auth.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.core.auth.ShikimoriAuthState
import ru.vladsaybulin.core.auth.ShikimoriAuthStateImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    @Singleton
    fun bindShikimoriAuthState(authState: ShikimoriAuthStateImpl): ShikimoriAuthState

}