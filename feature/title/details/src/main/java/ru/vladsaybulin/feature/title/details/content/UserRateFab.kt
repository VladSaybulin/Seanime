package ru.vladsaybulin.feature.title.details.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui.notNoneUserRateStatusIcon
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.feature.title.details.R
import ru.vladsaybulin.feature.title.details.UserRateState
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
internal fun UserRateFab(
    userRateState: UserRateState,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userRateStatus = when (userRateState) {
        is UserRateState.Loading -> null
        is UserRateState.NoUserRate, is UserRateState.NotAuthorized -> UserRateStatus.None
        is UserRateState.Success -> userRateState.userRate.status
    }
    val transition = updateTransition(targetState = userRateStatus, label = "UserRateStatus")

    val animatedContainerColor by transition.animateColor(label = "ContainerColor") { status ->
        if (status != null && status != UserRateStatus.None) {
            SeanimeTheme.seanimeColors[status].container
        } else SeanimeTheme.colorScheme.primaryContainer
    }

    val animatedContentColor by transition.animateColor(label = "ContentColor") { status ->
        if (status != null && status != UserRateStatus.None) {
            SeanimeTheme.seanimeColors[status].onContainer
        } else SeanimeTheme.colorScheme.onPrimaryContainer
    }

    ExtendedFloatingActionButton(
        text = {
            transition.AnimatedContent { status ->
                when (status) {
                    null -> Text(stringResource(id = R.string.feature_title_details_user_rate_loading))
                    UserRateStatus.None -> Text(stringResource(id = R.string.feature_title_details_user_rate_add))
                    else -> Text(text = status.asString())
                }
            }
        },
        icon = {
            transition.AnimatedContent { status ->
                when (status) {
                    null -> CircularProgressIndicator()
                    UserRateStatus.None -> AddIcon()
                    else -> UserRateStatusIcon(status = status)
                }
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
private fun AddIcon() {
    Icon(
        imageVector = SeanimeIcons.Add,
        contentDescription = stringResource(id = R.string.feature_title_details_add_to_list)
    )
}

@Composable
private fun UserRateStatusIcon(status: UserRateStatus) {
    Icon(
        imageVector = notNoneUserRateStatusIcon(userRateStatus = status),
        contentDescription = status.asString()
    )
}