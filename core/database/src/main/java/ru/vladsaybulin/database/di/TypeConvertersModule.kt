package ru.vladsaybulin.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import ru.vladsaybulin.database.utils.TextRangesTypeConverter

@Module
@InstallIn(SingletonComponent::class)
@OptIn(ExperimentalSerializationApi::class)
class TypeConvertersModule {

    @Provides
    fun provideProtobuf(): ProtoBuf = ProtoBuf
}