package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import ru.vladsaybulin.core.ui.R

@Composable
@ReadOnlyComposable
fun chaptersAndVolumesString(
    volumes: Int,
    chapters: Int
): String? {
    val volumesText = if (volumes > 0) {
        pluralStringResource(id = R.plurals.volumes, count = volumes, volumes)
    } else null

    val chaptersText = if (chapters > 0) {
        pluralStringResource(id = R.plurals.chapters, count = chapters, chapters)
    } else null

    if (volumesText == null && chaptersText == null)
        return null

    return buildString {
        volumesText?.let { append(it) }
        chaptersText?.let {
            if (isNotEmpty()) append(", ")
            append(it)
        }
    }
}