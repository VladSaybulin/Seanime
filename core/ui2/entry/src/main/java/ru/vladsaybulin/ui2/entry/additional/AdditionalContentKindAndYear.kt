package ru.vladsaybulin.ui2.entry.additional

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AdditionalContentKindAndYear(
    kindStr: String?,
    year: Int?
) {
    val finalText = listOfNotNull(kindStr, year?.toString()).joinToString(separator = " • ")
    if (finalText.isNotEmpty()) {
        Text(finalText)
    }
}