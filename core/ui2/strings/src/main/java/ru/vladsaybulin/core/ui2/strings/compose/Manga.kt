package ru.vladsaybulin.core.ui2.strings.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui2.strings.MangaStrings
import ru.vladsaybulin.model.manga.MangaKind

@Composable
@ReadOnlyComposable
fun MangaKind.asString() =
    stringResource(MangaStrings.kindId(this))

@Composable
@ReadOnlyComposable
fun MangaKind.asStringOrNull(): String? {
    if (this == MangaKind.None) return null
    return asString()
}