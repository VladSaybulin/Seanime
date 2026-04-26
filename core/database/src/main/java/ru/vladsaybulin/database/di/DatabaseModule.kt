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

package ru.vladsaybulin.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vladsaybulin.database.SeanimeRoomDatabase
import ru.vladsaybulin.database.utils.StatsTypeConverter
import ru.vladsaybulin.database.utils.TextRangesTypeConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideShikiDatabase(
        @ApplicationContext applicationContext: Context,
        textRangesTypeConverter: TextRangesTypeConverter,
        statsTypeConverter: StatsTypeConverter,
    ): SeanimeRoomDatabase =
        Room.databaseBuilder(
            checkNotNull(applicationContext.applicationContext),
            SeanimeRoomDatabase::class.java,
            "seanime_database"
        )
            .addTypeConverter(textRangesTypeConverter)
            .addTypeConverter(statsTypeConverter)
            .build()

}