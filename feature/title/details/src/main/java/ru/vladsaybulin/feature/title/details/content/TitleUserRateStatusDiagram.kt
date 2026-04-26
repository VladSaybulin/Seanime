package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastSumBy
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun TitleUserRateStatusDiagram(
    stats: List<StatisticsItem<UserRateStatus>>
) {
    val diagramState = rememberUserRateStatusDiagramState(stats)

    val seanimeColors = SeanimeTheme.seanimeColors

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(DiagramHeight)
                .clip(SeanimeTheme.shapes.medium)
        ) {
            var start = 0f
            UserRateStatusOrder.fastForEach { status ->
                val fraction = diagramState[status]?.second ?: return@fastForEach
                drawLinearDiagramSection(start, fraction, seanimeColors[status].color)
                start += fraction
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            UserRateStatusOrder.fastForEach { status ->
                val count = diagramState[status]?.first ?: return@fastForEach
                StatusLegend(
                    color = seanimeColors[status].color,
                    status = status,
                    count = count
                )
            }
        }
    }
}

@Composable
private fun StatusLegend(
    color: Color,
    status: UserRateStatus,
    count: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(SolidColor(color))
        }

        Text(
            text = status.asString(),
            style = SeanimeTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 4.dp)
        )

        Text(
            text = count.toString(),
            style = SeanimeTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 4.dp)
                .graphicsLayer { alpha = 0.66f }
        )
    }
}

private fun DrawScope.drawLinearDiagramSection(start: Float, value: Float, color: Color) {
    val startX = size.width * start
    val width = size.width * value

    drawRect(
        brush = SolidColor(color),
        topLeft = Offset(x = startX - 1, y = 0f),
        size = Size(width = width + 1, height = size.height)
    )
}

typealias TitleUserRateStatusDiagramState = Map<UserRateStatus, Pair<Int, Float>>

@Composable
private fun rememberUserRateStatusDiagramState(
    stats: List<StatisticsItem<UserRateStatus>>
): TitleUserRateStatusDiagramState = remember {
    val sumCount = stats.fastSumBy { it.count }.toFloat()

    buildMap {
        stats.fastForEach { (status, count) ->
            if (count <= 0) return@fastForEach
            put(status, Pair(count, count / sumCount))
        }
    }
}

@Preview
@Composable
fun TitleUserRateStatusDiagramPreview() {
    SeanimeTheme {
        Surface {
            TitleUserRateStatusDiagram(stats = PreviewUserRateStatusStatistics)
        }
    }
}

internal val PreviewUserRateStatusStatistics = listOf(
    StatisticsItem(UserRateStatus.Completed, 4434),
    StatisticsItem(UserRateStatus.Planned, 1023),
    StatisticsItem(UserRateStatus.Watching, 12455),
    StatisticsItem(UserRateStatus.OnHold, 345),
    StatisticsItem(UserRateStatus.Dropped, 2301)
)

private val UserRateStatusOrder = listOf(
    UserRateStatus.Planned,
    UserRateStatus.Watching,
    UserRateStatus.Completed,
    UserRateStatus.OnHold,
    UserRateStatus.Dropped
)

private val DiagramHeight = 36.dp