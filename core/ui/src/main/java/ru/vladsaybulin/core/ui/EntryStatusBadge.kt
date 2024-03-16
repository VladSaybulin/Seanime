package ru.vladsaybulin.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.components.ShikimoriTextBadge
import ru.vladsaybulin.core.designsystem.components.TextBadgeDefaults
import ru.vladsaybulin.core.ui.colors.entryStatusColor
import ru.vladsaybulin.core.ui.strings.animeStatusString
import ru.vladsaybulin.core.ui.strings.mangaStatusString
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType

@Composable
fun EntryStatusBadge(
    status: EntryStatus,
    entryType: EntryType,
    modifier: Modifier = Modifier,
) {
    val statusColor = entryStatusColor(status)
    ShikimoriTextBadge(
        border = TextBadgeDefaults.border(color = statusColor.copy(alpha = 0.7f)),
        contentColor = statusColor,
        modifier = modifier
    ) {
        Text(
            text = checkNotNull(
                when (entryType) {
                    EntryType.Anime -> animeStatusString(status)
                    else -> mangaStatusString(status)
                }
            )
        )
    }
}