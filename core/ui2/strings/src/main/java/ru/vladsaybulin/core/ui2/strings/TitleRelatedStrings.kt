package ru.vladsaybulin.core.ui2.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

interface TitleRelatedStrings {
    fun titleStatus(status: EntryStatus): Int

    fun userStatus(status: UserRateStatus): Int
}

val LocalTitleRelatedStrings = compositionLocalOf<TitleRelatedStrings> {
    error("LocalTitleRelatedStrings not provided")
}

@Composable
fun ProvideTitleRelatedStringsByType(
    titleType: EntryType,
    content: @Composable () -> Unit
) {
    val strings: TitleRelatedStrings = when (titleType) {
        EntryType.Anime -> AnimeStrings
        EntryType.Manga -> MangaStrings
    }

    CompositionLocalProvider(
        value = LocalTitleRelatedStrings provides strings,
        content = content
    )
}
        