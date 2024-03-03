package ru.vladsaybulin.core.designsystem.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class EntryStatusColors(
    val anons: Color = Color.Unspecified,
    val ongoing: Color = Color.Unspecified,
    val released: Color = Color.Unspecified,
    val paused: Color = Color.Unspecified,
    val discontinued: Color = Color.Unspecified,
)

val LocalEntryStatusColors = compositionLocalOf<EntryStatusColors> {
    error("EntryStatusColors not provided")
}