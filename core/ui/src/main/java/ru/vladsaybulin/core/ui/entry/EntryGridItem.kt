package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.colors.onRateStatusContainer
import ru.vladsaybulin.core.ui.colors.rateStatusContainer
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.UserRateStatus

@Composable
internal fun EntryGridItem(
    modifier: Modifier,
    name: String,
    poster: Poster?,
    onClick: () -> Unit,
    contentPadding: PaddingValues = DefaultContentPadding,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    detailsContent: (@Composable () -> Unit)? = null,
) {
    val containerColor = rateStatusContainer(userRateStatus = userRateStatus)
    val contentColor = onRateStatusContainer(userRateStatus = userRateStatus)
    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
            ) {
                EntryPoster(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(PosterAspectRatio),
                    poster = poster
                )
                UserRateStatusIcon(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(UserRateStatusIconSize),
                    userRateStatus = userRateStatus
                )
            }
            Spacer(modifier = Modifier.height(ImageSpace))
            EntryItemName(
                name = name,
                style = nameTextStyle
            )
            if (detailsContent != null) {
                ProvideTextStyle(value = bodyTextStyle) {
                    detailsContent()
                }
            }
        }
    }
}

@Composable
internal fun SmallEntryGridItem(
    modifier: Modifier,
    name: String,
    poster: Poster?,
    onClick: () -> Unit,
    contentPadding: PaddingValues = DefaultContentPadding,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    detailsContent: (@Composable () -> Unit)? = null,
) {
    EntryGridItem(
        modifier = modifier,
        name = name,
        poster = poster,
        onClick = onClick,
        contentPadding = contentPadding,
        userRateStatus = userRateStatus,
        nameTextStyle = nameTextStyle,
        bodyTextStyle = bodyTextStyle,
        detailsContent = detailsContent
    )
}

private val DefaultContentPadding = PaddingValues(8.dp, 8.dp)
private val ImageSpace = 4.dp
private val UserRateStatusIconSize = 32.dp
private const val PosterAspectRatio = 3 / 4f