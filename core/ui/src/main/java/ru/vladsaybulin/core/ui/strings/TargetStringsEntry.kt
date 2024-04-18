package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.compositionLocalOf

enum class TargetStringsEntry {
    Anime, Manga
}

val LocalTargetStringsEntry = compositionLocalOf {
    TargetStringsEntry.Anime
}
