package ru.vladsaybulin.core.ui2.score

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun ScoreExpanded(
    score: Float,
    modifier: Modifier = Modifier,
    iconSize: Dp = ScoreStarsDefaults.Size,
    tint: Color = ScoreStarsDefaults.Tint
) {
    Row {
        ScoreStars(
            score = score,
            modifier = Modifier.weight(1f),
            iconSize = iconSize,
            tint = tint
        )

        ScoreValue(score)
    }
}