package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.asRating

class AnimeRatingTypeConverter {

    @TypeConverter
    fun animeRatingToString(value: AnimeRating) = value.serializedName

    @TypeConverter
    fun stringToAnimeRating(value: String) = value.asRating()

}