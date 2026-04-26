package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui2.strings.R
import java.text.DecimalFormat

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

            Text(
                text = scoreFormatted(value, formatStyle),
                style = numberStyle
            )
        }

        if (showDescription) {
            Text(
                text = stringResource(scoreDescription(value)),
                style = descriptionStyle
            )
        }
    }
}

private fun scoreFormatted(value: Float, style: ScoreFormatStyle): String {
    val formatter = when (style) {
        ScoreFormatStyle.Integer -> IntegerFormatter
        ScoreFormatStyle.Real -> RealFormatter
    }

    return formatter.format(value)
}

private fun scoreDescription(score: Float) = when {
    score < 2f -> R.string.core_ui2_score_description_terribly
    score < 4f -> R.string.core_ui2_score_description_badly
    score < 6f -> R.string.core_ui2_score_description_normally
    score < 8f -> R.string.core_ui2_score_description_good
    score < 10f -> R.string.core_ui2_score_description_great
    else -> R.string.core_ui2_score_description_impossible
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

private val IntegerFormatter = DecimalFormat("#")
private val RealFormatter = DecimalFormat("#.##")