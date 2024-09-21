package ru.vladsaybulin.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.common.auth.LogoutAction
import ru.vladsaybulin.data.util.ShikimoriLogoutAction

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindLogoutAction(logoutAction: ShikimoriLogoutAction): LogoutAction
}