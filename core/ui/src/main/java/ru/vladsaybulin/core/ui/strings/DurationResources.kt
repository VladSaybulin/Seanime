package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.search.Duration

@Composable
fun durationString(duration: Duration) = stringResource(id = durationStringId(duration))

fun durationStringId(duration: Duration) = when (duration) {
    Duration.S -> R.string.core_ui_duration_s
    Duration.D -> R.string.core_ui_duration_d
    Duration.F -> R.string.core_ui_duration_f
}