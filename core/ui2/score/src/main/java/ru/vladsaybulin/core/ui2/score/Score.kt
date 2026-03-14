package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import java.text.DecimalFormat

enum class ScoreFormat {
    Integer, Real
}

@Composable
fun Score(
    score: Float,
    modifier: Modifier = Modifier,
    format: ScoreFormat = ScoreFormat.Real,
    style: TextStyle = ScoreDefaults.NumberStyle,
    leading: (@Composable () -> Unit)? = { ScoreDefaults.StarIcon() }
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        leading?.invoke()
        ScoreValue(score, format = format, style = style)
    }
}

object ScoreDefaults {
    val StarTintColor: Color
        @Composable get() = SeanimeTheme.colorScheme.primary

    val NumberStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.headlineSmall

    val DescriptionStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.labelSmall

    val DescriptionAlignment: Alignment.Horizontal = Alignment.CenterHorizontally

    @Composable
    fun StarIcon(
        tint: Color = StarTintColor
    ) {
        Icon(
            imageVector = SeanimeIcons.Star,
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
private fun ScoreValue(
    score: Float,
    modifier: Modifier = Modifier,
    format: ScoreFormat = ScoreFormat.Real,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier,
        text = scoreFormatted(score, format),
        style = style
    )
}

private fun scoreFormatted(value: Float, style: ScoreFormat): String {
    val formatter = when (style) {
        ScoreFormat.Integer -> IntegerFormatter
        ScoreFormat.Real -> RealFormatter
    }

    return formatter.format(value)
}

@Preview
@Composable
fun ScorePreview() {
    SeanimeTheme {
        Score(score = 8.5f)
    }
}

private val IntegerFormatter = DecimalFormat("#")
private val RealFormatter = DecimalFormat("#.##")