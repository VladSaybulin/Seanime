package ru.vladsaybulin.feature.userrate.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.core.ui.notNoneUserRateStatusIcon
import ru.vladsaybulin.core.ui.strings.animeUserRateStatusString
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun UserRateStatusButton(
    userRateStatus: UserRateStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = notNoneUserRateStatusIcon(userRateStatus),
    colors: UserRateStatusButtonColors = UserRateStatusButtonDefaults.userRateStatusButtonColors()
) {
    Row(
        modifier = modifier
            .defaultMinSize(minWidth = MinWidth, minHeight = MinHeight)
            .clip(CircleShape)
            .background(colors.containerColor)
            .clickable(
                onClick = onClick,
                role = Role.Button
            )
            .padding(ContentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val userRateText = checkNotNull(animeUserRateStatusString(userRateStatus = userRateStatus))
        Icon(imageVector = icon, contentDescription = userRateText)
        Spacer(modifier = Modifier.width(IconSpacing))
        Text(
            text = userRateText,
            style = ShikimoriTheme.typography.labelLarge,
            color = colors.contentColor
        )
    }
}

object UserRateStatusButtonDefaults {

    @Composable
    fun userRateStatusButtonColors(
        containerColor: Color = ShikimoriTheme.colorScheme
            .surfaceColorAtElevation(LocalAbsoluteTonalElevation.current),
        contentColor: Color = ShikimoriTheme.colorScheme.onSurface
    ) = UserRateStatusButtonColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun userRateStatusButtonColors(userRateStatus: UserRateStatus) =
        UserRateStatusButtonColors(
            containerColor = userRateStatusContainerColor(userRateStatus),
            contentColor = onUserRateStatusContainerColor(userRateStatus)
        )
}

data class UserRateStatusButtonColors(
    val containerColor: Color,
    val contentColor: Color
)

@Composable
@Preview
fun UserRateStatusButtonPreview() {
    ShikimoriTheme {
        UserRateStatusButton(
            userRateStatus = UserRateStatus.Watching,
            onClick = { },
        )
    }
}

val MinWidth = 58.dp
val MinHeight = 40.dp
private val ContentPadding = PaddingValues(start = 16.dp, end = 24.dp)
private val IconSpacing = 8.dp