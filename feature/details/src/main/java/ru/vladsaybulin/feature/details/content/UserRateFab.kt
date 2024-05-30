package ru.vladsaybulin.feature.details.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.core.ui.notNoneUserRateStatusIcon
import ru.vladsaybulin.core.ui.strings.userRateStatusString
import ru.vladsaybulin.feature.details.R
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
internal fun UserRateFab(
    userRateStatus: UserRateStatus?,
    entryType: EntryType,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = userRateStatus, label = "UserRateStatus")

    val animatedContainerColor by transition.animateColor(label = "ContainerColor") { status ->
        if (status == null || status == UserRateStatus.None) {
            SeanimeTheme.colorScheme.primaryContainer
        } else userRateStatusContainerColor(userRateStatus = status)
    }

    val animatedContentColor by transition.animateColor(label = "ContentColor") { status ->
        if (status == null || status == UserRateStatus.None) {
            SeanimeTheme.colorScheme.onPrimaryContainer
        } else onUserRateStatusContainerColor(userRateStatus = status)
    }

    ExtendedFloatingActionButton(
        text = {
            transition.AnimatedContent { status ->
                UserRateFabText(status = status, entryType = entryType)
            }
        },
        icon = {
            transition.AnimatedContent { status ->
                UserRateFabIcon(status = status)
            }
        },
        onClick = onClick,
        expanded = expanded,
        containerColor = animatedContainerColor,
        contentColor = animatedContentColor,
        modifier = modifier
    )
}

@Composable
private fun UserRateFabText(
    status: UserRateStatus?,
    entryType: EntryType,
    modifier: Modifier = Modifier
) {
    val text = if (status == null || status == UserRateStatus.None) {
        stringResource(id = R.string.add)
    } else userRateStatusString(status)

    Text(
        text = text,
        modifier = modifier
    )
}

@Composable
private fun UserRateFabIcon(
    status: UserRateStatus?,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = if (status == null || status == UserRateStatus.None) {
            SeanimeIcons.Add
        } else notNoneUserRateStatusIcon(userRateStatus = status),
        contentDescription = null,
        modifier = modifier
    )

}