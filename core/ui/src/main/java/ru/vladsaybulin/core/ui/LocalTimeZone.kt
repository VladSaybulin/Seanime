package ru.vladsaybulin.core.ui

import androidx.compose.runtime.compositionLocalOf
import kotlinx.datetime.TimeZone

val LocalTimeZone = compositionLocalOf {
    TimeZone.currentSystemDefault()
}