package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun TitleName(
    name: String,
    russianName: String?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = russianName ?: name,
            style = SeanimeTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        if (russianName != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                style = SeanimeTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        }
    }
}