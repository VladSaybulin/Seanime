package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastForEachIndexed
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.model.common.StatisticsItem
import java.text.DecimalFormat
import kotlin.math.max

@Composable
internal fun TitleScore(
    score: Float,
    stats: List<StatisticsItem<Int>>
) {
    Layout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        content = {
            Icon(
                imageVector = SeanimeIcons.Star,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .layoutId(IconId)
            )

            Text(
                text = formattedScoreValue(score),
                style = SeanimeTheme.typography.displaySmall,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .layoutId(ScoreValueId)
            )

            ScoreDiagram(
                stats = stats,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .layoutId(DiagramId),
            )
        },
        measurePolicy = { measurables, constraint ->

            val looseConstraints = constraint.copy(minWidth = 0, minHeight = 0)
            var occupiedSpaceHorizontally = 0

            val iconPlaceable = measurables.first { it.layoutId == IconId }
                .measure(looseConstraints)
            occupiedSpaceHorizontally += iconPlaceable.width

            val scorePlaceable = measurables.first { it.layoutId == ScoreValueId }
                .measure(looseConstraints.offset(horizontal = -occupiedSpaceHorizontally))
            occupiedSpaceHorizontally += scorePlaceable.width

            val diagramPlaceable = measurables.first { it.layoutId == DiagramId }
                .measure(looseConstraints.offset(horizontal = -occupiedSpaceHorizontally))


            val width = occupiedSpaceHorizontally + diagramPlaceable.width
            val height = max(max(iconPlaceable.height, scorePlaceable.height), diagramPlaceable.height)

            layout(width, height) {
                iconPlaceable.placeRelative(
                    x = 0,
                    y = (height - iconPlaceable.height) / 2
                )

                scorePlaceable.placeRelative(
                    x = iconPlaceable.width,
                    y = (height - scorePlaceable.height) / 2
                )

                diagramPlaceable.placeRelative(
                    x = occupiedSpaceHorizontally,
                    y = (height - diagramPlaceable.height) / 2
                )
            }
        }
    )
}

@Composable
private fun ScoreDiagram(
    stats: List<StatisticsItem<Int>>,
    modifier: Modifier = Modifier,
    shape: CornerBasedShape = RoundedCornerShape(4.dp),
    color: Color = SeanimeTheme.colorScheme.secondary,
    trackColor: Color = SeanimeTheme.colorScheme.outlineVariant,
    minBarHeight: Dp = 8.dp
) {
    val normalizedStats = rememberNormalizedScoreStats(stats = stats)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        normalizedStats.fastForEachIndexed { i, value ->
            Column(modifier = Modifier.weight(1f)) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                ) {
                    drawScoreStatsBar(
                        value = 1f,
                        shape = shape,
                        color = trackColor,
                        minHeight = minBarHeight.toPx()
                    )
                    drawScoreStatsBar(
                        value = value,
                        shape = shape,
                        color = color,
                        minHeight = minBarHeight.toPx()
                    )
                }

                Text(
                    text = (i + 1).toString(),
                    style = SeanimeTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

private fun DrawScope.drawScoreStatsBar(
    value: Float,
    shape: androidx.compose.ui.graphics.Shape,
    color: Color,
    minHeight: Float
) {
    val barSize = androidx.compose.ui.geometry.Size(
        width = size.width,
        height = (size.height * value).coerceAtLeast(minHeight)
    )

    translate(
        top = size.height - barSize.height
    ) {
        drawOutline(
            outline = shape.createOutline(barSize, layoutDirection, this),
            style = Fill,
            brush = SolidColor(color)
        )
    }
}

@Composable
private fun rememberNormalizedScoreStats(stats: List<StatisticsItem<Int>>): List<Float> = remember(stats) {
    MutableList(10) { 0.0f }.apply {
        val maxCount = stats.maxOf { it.count }.toFloat()
        stats.forEach { (score, count) ->
            this@apply[score - 1] = count / maxCount
        }
    }.also { println(it.joinToString()) }
}

private fun scoreValueText(score: Float) = when {
    score < 2f -> "Ужасно"
    score < 4f -> "Плохо"
    score < 6f -> "Нормально"
    score < 8f -> "Хорошо"
    score <= 10f -> "Отлично"
    else -> ""
}

private fun formattedScoreValue(score: Float): String = DecimalFormat("#.00").format(score)

@Preview(widthDp = 400)
@Composable
fun TitleScorePreview() {
    SeanimeTheme {
        Surface() {
            TitleScore(score = 8.82f, stats = PreviewScoreStatistics)
        }
    }
}

internal val PreviewScoreStatistics = listOf(
    StatisticsItem(1, 34),
    StatisticsItem(2, 56),
    StatisticsItem(3, 76),
    StatisticsItem(4, 32),
    StatisticsItem(5, 14),
    StatisticsItem(6, 67),
    StatisticsItem(7, 98),
    StatisticsItem(8, 155),
    StatisticsItem(9, 234),
    StatisticsItem(10, 203),
)

private val IconId = "icon"
private val ScoreValueId = "value"
private val ValueTextId = "value_text"
private val DiagramId = "stats"