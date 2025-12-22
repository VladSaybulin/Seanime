package ru.vladsaybulin.core.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.components.ShikimoriTextBadge
import ru.vladsaybulin.core.designsystem.components.TextBadgeDefaults
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui.strings.entryStatusString
import ru.vladsaybulin.model.common.EntryStatus

@Composable
fun EntryStatusBadge(
    status: EntryStatus,
    modifier: Modifier = Modifier,
) {
    val statusColor = SeanimeTheme.seanimeColors[status]
    ShikimoriTextBadge(
        border = TextBadgeDefaults.border(color = statusColor.copy(alpha = 0.7f)),
        contentColor = statusColor,
        modifier = modifier
    ) {
        Text(text = entryStatusString(status))
    }
}