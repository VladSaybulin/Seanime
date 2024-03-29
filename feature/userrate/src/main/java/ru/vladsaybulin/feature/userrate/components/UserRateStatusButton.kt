package ru.vladsaybulin.feature.userrate.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.core.ui.strings.animeUserRateStatusString
import ru.vladsaybulin.core.ui.userRateStatusIcon
import ru.vladsaybulin.model.UserRateStatus

@Composable
fun UserRateStatusButton(
    userRateStatus: UserRateStatus,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: UserRateStatusButtonColors = UserRateStatusButtonDefaults.userRateStatusButtonColors(
        selectedContainerColor = userRateStatusContainerColor(userRateStatus = userRateStatus),
        selectedContentColor = onUserRateStatusContainerColor(userRateStatus = userRateStatus)
    ),
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = checkNotNull(userRateStatusIcon(userRateStatus = userRateStatus)),
            contentDescription = null
        )
    },
    text: @Composable () -> Unit = {
        Text(text = checkNotNull(animeUserRateStatusString(userRateStatus = userRateStatus)))
    }
) {
    UserRateStatusButton(
        selected = selected,
        colors = colors,
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        text = text
    )
}

@Composable
fun UserRateStatusButton(
    selected: Boolean,
    colors: UserRateStatusButtonColors,
    onClick: () -> Unit,
    modifier: Modifier,
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit
) {
    val colorTransition = updateTransition(targetState = selected, label = "UserRateButtonColor")
    val containerColor by colorTransition.animateColor(label = "containerColor") {
        colors.containerColor(it)
    }
    val contentColor by colorTransition.animateColor(label = "contentColor") {
        colors.contentColor(it)
    }

    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            icon()
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            text()
        }
    }
}

data class UserRateStatusButtonColors(
    val containerColor: Color,
    val selectedContainerColor: Color,
    val contentColor: Color,
    val selectedContentColor: Color
) {
    fun containerColor(selected: Boolean) = if (selected) {
        selectedContainerColor
    } else containerColor

    fun contentColor(selected: Boolean) = if (selected) {
        selectedContentColor
    } else contentColor
}

object UserRateStatusButtonDefaults {
    @Composable
    fun userRateStatusButtonColors(
        containerColor: Color = ShikimoriTheme.colorScheme.surface.copy(alpha = 0f),
        selectedContainerColor: Color = ShikimoriTheme.colorScheme.primaryContainer,
        contentColor: Color = ShikimoriTheme.colorScheme.onSurface,
        selectedContentColor: Color = ShikimoriTheme.colorScheme.onPrimaryContainer
    ) = UserRateStatusButtonColors(
        containerColor = containerColor,
        selectedContainerColor = selectedContainerColor,
        contentColor = contentColor,
        selectedContentColor = selectedContentColor
    )
}

@Preview
@Composable
fun UserRateStatusButtonPreview() {
    ShikimoriTheme {
        Surface {
            var selected by remember { mutableStateOf(false) }

            UserRateStatusButton(
                userRateStatus = UserRateStatus.Watching,
                selected = selected,
                onClick = { selected = !selected },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}