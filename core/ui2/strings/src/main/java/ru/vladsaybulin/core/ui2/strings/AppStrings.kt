package ru.vladsaybulin.core.ui2.strings

import ru.vladsaybulin.model.common.EntryType

object AppStrings {
    fun titleType(entryType: EntryType): Int = when (entryType) {
        EntryType.Anime -> R.string.core_ui2_strings_title_type_anime
        EntryType.Manga -> R.string.core_ui2_strings_title_type_anime
    }
}