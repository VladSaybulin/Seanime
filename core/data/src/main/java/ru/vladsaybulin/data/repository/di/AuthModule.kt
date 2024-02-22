package ru.vladsaybulin.data.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.data.repository.AuthRepository
import ru.vladsaybulin.network.common.TokensHolder

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {

    @Binds
    fun bindTokensHolder(authRepository: AuthRepository): TokensHolder

}