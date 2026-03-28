package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import java.text.DecimalFormat

/**
 * Format options for displaying score values.
 */
enum class ScoreFormat {
    /** Displays the score as an integer (e.g., 8). */
    Integer,
    /** Displays the score as a decimal (e.g., 8.5). */
    Real
}

/**
 * A basic score display component that shows a numeric value with an optional icon.
 *
 * @param score The numeric score value to display.
 * @param modifier The modifier to be applied to the layout.
 * @param format The format style for the score text.
 * @param style The text style to be applied to the score.
 * @param leading An optional leading composable, defaults to a star icon.
 */
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

/**
 * Default values and components for score-related UI.
 */
object ScoreDefaults {
    /** Default tint color for score icons. */
    val IconTintColor: Color
        @Composable get() = SeanimeTheme.colorScheme.primary

    /** Default text style for score numbers. */
    val NumberStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.headlineSmall

    /** Default size for score icons. */
    val IconSize = 48.dp

    /**
     * A standard star icon used for score displays.
     *
     * @param tint The color of the icon.
     * @param size The size of the icon.
     */
    @Composable
    fun StarIcon(
        tint: Color = IconTintColor,
        size: Dp = IconSize
    ) {
        Icon(
            imageVector = SeanimeIcons.Star,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(size)
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
private val RealFormatter = DecimalFormat("0.##")
