package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun Score(
    value: Float,
    modifier: Modifier = Modifier,
    formatStyle: ScoreFormatStyle = ScoreFormatStyle.Real,
    iconSize: Dp = DefaultScoreIconSize,
    showDescription: Boolean = false,
    numberStyle: TextStyle = SeanimeTheme.typography.headlineSmall,
    descriptionStyle: TextStyle = SeanimeTheme.typography.labelSmall
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = SeanimeIcons.Star,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = SeanimeTheme.colorScheme.primary
            )

            ScoreValue(value, formatStyle, numberStyle)
        }

        if (showDescription) {
            ScoreDescription(value, descriptionStyle)
        }
    }
}

@Preview
@Composable
fun ScorePreview() {
    SeanimeTheme {
        Surface {
            Score(6.87f)
        }
    }
}

@Preview
@Composable
fun ScorePreview_WithDescription() {
    SeanimeTheme {
        Surface {
            Score(6.87f, showDescription = true)
        }
    }
}

private val DefaultScoreIconSize = 24.dp