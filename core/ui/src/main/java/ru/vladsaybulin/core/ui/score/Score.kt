package ru.vladsaybulin.core.ui.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import java.text.DecimalFormat

enum class ScoreNumberFormatStyle {
    Int, Float
}

@Composable
fun Score(
    score: Float,
    modifier: Modifier = Modifier,
    numberFormatStyle: ScoreNumberFormatStyle = ScoreNumberFormatStyle.Float,
    showLabel: Boolean = true,
    numberStyle: TextStyle = ShikimoriTheme.typography.headlineSmall,
    labelStyle: TextStyle = ShikimoriTheme.typography.labelSmall,
    starSize: DpSize = DefaultStarSize,
    color: Color = ShikimoriTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ScoreStars(
            score = score,
            starSize = starSize,
            color = color
        )

        ScoreText(
            score = score,
            numberFormatStyle = numberFormatStyle,
            showLabel = showLabel,
            numberStyle = numberStyle,
            labelStyle = labelStyle,
        )
    }
}

@Composable
fun ScoreInput(
    score: () -> Int,
    onScoreChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
    numberStyle: TextStyle = ShikimoriTheme.typography.headlineSmall,
    labelStyle: TextStyle = ShikimoriTheme.typography.labelSmall,
    starSize: DpSize = DefaultStarSize,
    color: Color = ShikimoriTheme.colorScheme.primary
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val currentScore = score()
        AnimatedScoreStars(
            score = currentScore.toFloat(),
            starSize = starSize,
            color = color,
            modifier = Modifier.inputScore {
                if (it == currentScore) return@inputScore
                onScoreChange(it)
            }
        )
        if (currentScore > 0) {
            ScoreText(
                score = currentScore.toFloat(),
                numberFormatStyle = ScoreNumberFormatStyle.Int,
                showLabel = showLabel,
                numberStyle = numberStyle,
                labelStyle = labelStyle,
                modifier = Modifier.defaultMinSize(minWidth = 72.dp)
            )
        }
    }
}

@Composable
fun ScoreText(
    score: Float,
    numberFormatStyle: ScoreNumberFormatStyle,
    showLabel: Boolean,
    numberStyle: TextStyle,
    labelStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = scoreFormatted(score, numberFormatStyle),
            style = numberStyle
        )
        if (showLabel) {
            Text(
                text = scoreLabel(score),
                style = labelStyle,
                modifier = Modifier
            )
        }
    }
}

fun scoreLabel(score: Float) = when {
    score < 2f -> "Ужасно"
    score < 4f -> "Плохо"
    score < 6f -> "Нормально"
    score < 8f -> "Хорошо"
    score <= 10f -> "Отлично"
    else -> ""
}

fun scoreFormatted(score: Float, style: ScoreNumberFormatStyle): String = when (style) {
    ScoreNumberFormatStyle.Int -> score.toInt().toString()
    ScoreNumberFormatStyle.Float -> DecimalFormat("#.00").format(score)
}