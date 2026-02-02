package ru.vladsaybulin.core.ui2.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.res.stringResource
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
@ReadOnlyComposable
fun EntryStatus.asString() = stringResource(id = LocalTitleStrings.current.titleStatusId(this))

@Composable
@ReadOnlyComposable
fun UserRateStatus.asString() = stringResource(id = LocalTitleStrings.current.userStatusId(this))


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
