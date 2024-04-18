package ru.vladsaybulin.database.models

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.user.UserImage

/**
 * Embedded in UserBdo
 */
data class UserImageDbo(
    @ColumnInfo("x160_url") val x160Url: String,
    @ColumnInfo("x148_url") val x148Url: String,
    @ColumnInfo("x80_url") val x80Url: String,
    @ColumnInfo("x64_url") val x64Url: String,
    @ColumnInfo("x48_url") val x48Url: String,
    @ColumnInfo("x32_url") val x32Url: String,
    @ColumnInfo("x16_url") val x16Url: String
)

fun UserImageDbo.asExternalModel() = UserImage(
    x160Url = x160Url,
    x148Url = x148Url,
    x80Url = x80Url,
    x64Url = x64Url,
    x48Url = x48Url,
    x32Url = x32Url,
    x16Url = x16Url
)