package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.ContentWithHeader
import ru.vladsaybulin.core.ui.Header
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.common.StatisticsItem
import java.text.DecimalFormat

fun LazyListScope.score(
    score: Float,
    statistics: List<StatisticsItem<Int>>
) {
    item(key = "score") {
        ScoreContent(score = score, statistics = statistics)
    }
}

@Composable
fun ScoreContent(
    score: Float,
    statistics: List<StatisticsItem<Int>>
) {
    ContentWithHeader(
        header = {
            Header(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(stringResource(R.string.feature_details_score))
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ScoreValue(score = score)
            Spacer(modifier = Modifier.width(16.dp))
            ScoreHistogram(statistics = statistics)
        }
    }
}

@Composable
fun ScoreValue(score: Float) {
    Column(modifier = Modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
        ) {
            val scoreFormatter = DecimalFormat("#.00")
            Icon(
                imageVector = ShikimoriIcons.Star,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = scoreFormatter.format(score),
                style = SeanimeTheme.typography.headlineMedium
            )
        }
        Text(
            text = "Отлично",
            style = SeanimeTheme.typography.labelSmall,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(),
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun ScoreHistogram(
    statistics: List<StatisticsItem<Int>>
) {
    val color = SeanimeTheme.colorScheme.primary
    val outlineColor = SeanimeTheme.colorScheme.outlineVariant

    val maxCount = statistics.maxOfOrNull { it.count } ?: 0

    Column {
        Row(
            modifier = Modifier.height(64.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 12.dp)
        ) {
            for (i in (1..10)) {
                val count = statistics.firstOrNull { it.values == i }?.count ?: 0
                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStartPercent = 25, topEndPercent = 25))
                ) {
                    //Background
                    drawRoundRect(brush = SolidColor(outlineColor))

                    val cornerRadius = size.width * 0.5f
                    val minHeight = cornerRadius / 2
                    val totalHeight = size.height - minHeight

                    val scoreHeight = (count * totalHeight / maxCount)
                    val y = size.height - scoreHeight

                    drawRoundRect(
                        size = size.copy(height = cornerRadius),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        brush = SolidColor(color),
                        topLeft = Offset(x = 0f, y = y - minHeight)
                    )

                    drawRoundRect(
                        size = size.copy(height = scoreHeight),
                        topLeft = Offset(0f, y),
                        brush = SolidColor(color)
                    )
                }
            }
        }
        HorizontalDivider()
        Row(horizontalArrangement = Arrangement.spacedBy(space = 12.dp)) {
            for (i in (1..10)) {
                Text(
                    text = i.toString(),
                    modifier = Modifier.weight(1f),
                    style = SeanimeTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = SeanimeTheme.colorScheme.outline
                )
            }
        }
    }
}

@Preview
@Composable
fun ScoreContentPreview() {
    SeanimeTheme {
        Surface {
            val statistics = listOf(
                StatisticsItem(1, 34),
                StatisticsItem(2, 56),
                StatisticsItem(3, 76),
                StatisticsItem(4, 32),
                StatisticsItem(5, 24),
                StatisticsItem(6, 67),
                StatisticsItem(7, 98),
                StatisticsItem(8, 155),
                StatisticsItem(9, 234),
                StatisticsItem(10, 203),
            )

            val score = 7.9f

            ScoreContent(score, statistics)
        }
    }
}