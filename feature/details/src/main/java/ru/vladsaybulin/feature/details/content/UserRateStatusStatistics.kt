package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.ContentWithHeader
import ru.vladsaybulin.core.ui.Header
import ru.vladsaybulin.core.ui.colors.userRateStatusColor
import ru.vladsaybulin.core.ui.strings.userRateStatusString
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.userrate.UserRateStatus

fun LazyListScope.userRateStatusDiagram(statisticItems: List<StatisticsItem<UserRateStatus>>) {
    item(key = "diagram") {
        UserRateStatusStatisticsContent(statisticItems)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserRateStatusStatisticsContent(
    statisticItems: List<StatisticsItem<UserRateStatus>>
) {
    ContentWithHeader(
        header = {
            Header(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(stringResource(R.string.feature_details_user_statuses))
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            UserRateStatusStatisticsDiagram(statisticItems = statisticItems)
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 24.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                statisticItems.fastForEach {
                    UserRateStatusLegend(statisticItem = it)
                }
            }
        }
    }
}

@Composable
fun UserRateStatusLegend(
    statisticItem: StatisticsItem<UserRateStatus>
) {
    Row {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(userRateStatusColor(userRateStatus = statisticItem.values))
        )
        Text(
            text = userRateStatusString(userRateStatus = statisticItem.values),
            style = SeanimeTheme.typography.labelLarge,
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .alpha(0.5f)
        )

        Text(
            text = statisticItem.count.toString(),
            style = SeanimeTheme.typography.labelLarge
        )
    }
}

@Composable
fun UserRateStatusStatisticsDiagram(
    statisticItems: List<StatisticsItem<UserRateStatus>>
) {
    val statItemToColor = statisticItems.map {
        it to userRateStatusColor(userRateStatus = it.values)
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(24.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        val total = statItemToColor.sumOf { it.first.count }
        statItemToColor.forEach {
            val width = it.first.count * size.width / total
            drawRect(
                brush = SolidColor(it.second),
                size = size.copy(width = width)
            )
            drawContext.transform.translate(left = width)
        }
    }
}

@Composable
@Preview
fun UserRateStatusStatisticsPreview() {
    SeanimeTheme {
        Surface {
            UserRateStatusStatisticsContent(
                listOf(
                    StatisticsItem(UserRateStatus.Planned, 45),
                    StatisticsItem(UserRateStatus.Watching, 12),
                    StatisticsItem(UserRateStatus.Completed, 100),
                    StatisticsItem(UserRateStatus.OnHold, 16),
                    StatisticsItem(UserRateStatus.Dropped, 4)
                )
            )
        }
    }
}