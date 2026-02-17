package ru.vladsaybulin.ui2.entry.additional

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.vladsaybulin.core.designsystem.components.SeanimeTag
import ru.vladsaybulin.core.designsystem.components.TagDefaults
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.model.common.EntryStatus

@Composable
internal fun AdditionalContentStatusTag(
    status: EntryStatus,
    modifier: Modifier = Modifier
) {
    if (status != EntryStatus.None) {
        val color = SeanimeTheme.seanimeColors[status]
        SeanimeTag(
            border = TagDefaults.border(color = color),
            contentColor = color,
            modifier = modifier
        ) {
            Text(status.asString())
        }
    }
}