package ru.vladsaybulin.core.ui2.strings.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui2.strings.AnimeStrings
import ru.vladsaybulin.core.ui2.strings.R
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating

@Composable
@ReadOnlyComposable
fun AnimeKind.asString(): String =
    stringResource(id = AnimeStrings.kindId(this))

@Composable
@ReadOnlyComposable
fun AnimeKind.asStringOrNull(): String? {
    if (this == AnimeKind.None) return null
    return asString()
}

@Composable
@ReadOnlyComposable
fun AnimeRating.asString() =
    stringResource(id = AnimeStrings.ratingId(this))