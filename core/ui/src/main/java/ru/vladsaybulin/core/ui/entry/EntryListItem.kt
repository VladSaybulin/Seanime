package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainer
import ru.vladsaybulin.core.ui.colors.userRateStatusContainer
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.UserRateStatus

@Composable
internal fun EntryListItem(
    modifier: Modifier = Modifier,
    name: String,
    poster: Poster?,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = MaterialTheme.typography.titleMedium,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    onClick: () -> Unit,
    detailsContent: (@Composable () -> Unit)? = null
) {
    val containerColor = userRateStatusContainer(userRateStatus = userRateStatus)
    val contentColor = onUserRateStatusContainer(userRateStatus = userRateStatus)

    Surface(
        modifier = modifier,
        onClick = onClick,
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small,
    ) {
        Row(modifier = Modifier.padding(DefaultContentPadding)) {
            Box(
                modifier = Modifier
                    .width(PosterWidth)
                    .aspectRatio(3 / 4f)
                    .clip(MaterialTheme.shapes.extraSmall)
            ) {
                EntryPoster(
                    modifier = Modifier
                        .width(PosterWidth)
                        .aspectRatio(PosterAspectRatio),
                    poster = poster
                )
                if (userRateStatus != UserRateStatus.None) {
                    UserRateStatusBadge(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(UserRateStatusIconSize),
                        userRateStatus = userRateStatus
                    )
                }
            }

            Spacer(modifier = Modifier.width(ImageSpace))

            Column {
                EntryItemName(
                    name = name,
                    style = nameTextStyle
                )
                if (detailsContent != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    ProvideTextStyle(value = bodyTextStyle) {
                        detailsContent()
                    }
                }
            }
        }
    }
}

private val DefaultContentPadding = PaddingValues(8.dp, 8.dp)
private val ImageSpace = 8.dp
private val PosterWidth = 86.dp
private val UserRateStatusIconSize = 32.dp
private const val PosterAspectRatio = 3 / 4f