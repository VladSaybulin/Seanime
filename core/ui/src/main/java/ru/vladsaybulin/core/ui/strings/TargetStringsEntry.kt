package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.compositionLocalOf
import ru.vladsaybulin.model.common.EntryType

enum class TargetStringsEntry {
    Anime, Manga
}

val LocalTargetStringsEntry = compositionLocalOf {
    TargetStringsEntry.Anime
}

fun EntryType.asTargetStringEntry() = when (this) {
    EntryType.Anime -> TargetStringsEntry.Anime
    EntryType.Manga -> TargetStringsEntry.Manga
}