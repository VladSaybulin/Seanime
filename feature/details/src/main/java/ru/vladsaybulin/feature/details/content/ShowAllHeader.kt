package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.feature.details.R

@Composable
fun ShowAllHeaderText(
    headerText: String,
    modifier: Modifier = Modifier,
    showAllText: String = stringResource(id = R.string.show_more),
    shouldShownShowAll: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(headerText)
        if (shouldShownShowAll) {
            Text(showAllText, color = SeanimeTheme.colorScheme.primary)
        }

    }
}