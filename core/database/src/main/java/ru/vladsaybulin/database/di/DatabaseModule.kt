package ru.vladsaybulin.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.database.ShikiRoomDatabase
import ru.vladsaybulin.database.utils.TextRangesTypeConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideShikiDatabase(
        @ApplicationContext applicationContext: Context,
        textRangesTypeConverter: TextRangesTypeConverter
    ): ShikiRoomDatabase =
        Room.databaseBuilder(
            checkNotNull(applicationContext.applicationContext),
            ShikiRoomDatabase::class.java,
            "shiki_database"
        )
            .addTypeConverter(textRangesTypeConverter)
            .build()

}