package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

@Composable
fun EntryDetailsName(
    name: String,
    russianName: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = russianName ?: name,
            style = ShikimoriTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        if (russianName != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = ShikimoriTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        }
    }
}