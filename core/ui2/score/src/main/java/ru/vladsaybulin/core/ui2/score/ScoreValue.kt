package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import java.text.DecimalFormat

@Composable
internal fun ScoreValue(
    value: Float,
    formatStyle: ScoreFormatStyle = ScoreFormatStyle.Real,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        text = scoreFormatted(value, formatStyle),
        style = style
    )
}

@Composable
internal fun ScoreDescription(value: Float, style: TextStyle) {
    Text(
        text = stringResource(scoreDescription(value)),
        style = style
    )
}

@Composable
internal fun ScoreValueWithDescription(
    value: Float,
    formatStyle: ScoreFormatStyle = ScoreFormatStyle.Real,
    style: TextStyle,
    descriptionStyle: TextStyle
) {
    Column {
        ScoreValue(value, formatStyle, style)
        ScoreDescription(value = value, style = descriptionStyle)
    }
}

private fun scoreDescription(score: Float) = when {
    score < 2f -> R.string.core_ui2_score_description_terribly
    score < 4f -> R.string.core_ui2_score_description_badly
    score < 6f -> R.string.core_ui2_score_description_normally
    score < 8f -> R.string.core_ui2_score_description_good
    score < 10f -> R.string.core_ui2_score_description_great
    else -> R.string.core_ui2_score_description_impossible
}

private fun scoreFormatted(value: Float, style: ScoreFormatStyle): String {
    val formatter = when (style) {
        ScoreFormatStyle.Integer -> IntegerFormatter
        ScoreFormatStyle.Real -> RealFormatter
    }

    return formatter.format(value)
}

private val IntegerFormatter = DecimalFormat("#")
private val RealFormatter = DecimalFormat("#.##")
