package ru.vladsaybulin.core.ui2.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

interface DependsOnTitleStrings {
    fun titleStatusId(status: EntryStatus): Int

    fun userStatusId(status: UserRateStatus): Int
}

val LocalTitleStrings = compositionLocalOf<DependsOnTitleStrings> {
    error("LocalTitleRelatedStrings not provided")
}

@Composable
fun ProvideTitleStringsByType(
    titleType: EntryType,
    content: @Composable () -> Unit
) {
    val strings: DependsOnTitleStrings = when (titleType) {
        EntryType.Anime -> AnimeStrings
        EntryType.Manga -> MangaStrings
    }

    CompositionLocalProvider(
        value = LocalTitleStrings provides strings,
        content = content
    )
}
