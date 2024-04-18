package ru.vladsaybulin.core.ui.entry

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.EntryPoster
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.model.Poster
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.common.Image

@Composable
fun EntryListItem(
    modifier: Modifier = Modifier,
    name: String,
    poster: Image?,
    userRateStatus: UserRateStatus = UserRateStatus.None,
    nameTextStyle: TextStyle = MaterialTheme.typography.labelLarge,
    bodyTextStyle: TextStyle = MaterialTheme.typography.bodySmall,
    posterWidth: Dp = DefaultPosterWidth,
    onClick: () -> Unit,
    detailsContent: (@Composable () -> Unit)? = null
) {
    val containerColor = userRateStatusContainerColor(userRateStatus = userRateStatus)
    val contentColor = onUserRateStatusContainerColor(userRateStatus = userRateStatus)

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
                    .width(DefaultPosterWidth)
                    .aspectRatio(3 / 4f)
                    .clip(MaterialTheme.shapes.extraSmall)
            ) {
                EntryPoster(
                    modifier = Modifier.width(posterWidth),
                    poster = poster
                )
                if (userRateStatus != UserRateStatus.None) {
                    UserRateStatusBadge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
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

@Preview
@Composable
fun EntryListItemPreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "Entry name",
            poster = Poster(""),
            onClick = { },
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = UserRateStatus.Watching,
            detailsContent = {
                Column {
                    Text(text = "Details text 1")
                    Text(text = "Details text 2")
                    Text(text = "Details text 3")
                }
            }
        )
    }
}

@Preview
@Composable
fun EntryListWithLongNamePreview() {
    ShikimoriTheme {
        EntryListItem(
            name = "This is a long entry name that does not fit into 2 lines",
            poster = Poster(""),
            modifier = Modifier.fillMaxWidth(),
            userRateStatus = UserRateStatus.None,
            onClick = { },
        )
    }
}

private val DefaultContentPadding = PaddingValues(8.dp, 8.dp)
private val ImageSpace = 8.dp
private val UserRateStatusIconSize = 20.dp
private val DefaultPosterWidth = 72.dp