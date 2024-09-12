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
import ru.vladsaybulin.core.ui.colors.onUserRateStatusContainerColor
import ru.vladsaybulin.core.ui.colors.userRateStatusContainerColor
import ru.vladsaybulin.core.ui.notNoneUserRateStatusIcon
import ru.vladsaybulin.core.ui.strings.userRateStatusString
import ru.vladsaybulin.feature.title.details.R
import ru.vladsaybulin.feature.title.details.UserRateState
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
internal fun UserRateFab(
    userRateState: UserRateState,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = userRateState, label = "UserRateStatus")

    val animatedContainerColor by transition.animateColor(label = "ContainerColor") { state ->
        if (state is UserRateState.Success) {
            userRateStatusContainerColor(userRateStatus = state.userRate.status)
        } else SeanimeTheme.colorScheme.primaryContainer
    }

    val animatedContentColor by transition.animateColor(label = "ContentColor") { state ->
        if (state is UserRateState.Success) {
            onUserRateStatusContainerColor(userRateStatus = state.userRate.status)
        } else SeanimeTheme.colorScheme.onPrimaryContainer
    }

    ExtendedFloatingActionButton(
        text = {
            transition.AnimatedContent { state ->
                when (state) {
                    UserRateState.Loading -> Text(stringResource(id = R.string.feature_title_details_user_rate_loading))
                    is UserRateState.Success -> Text(text = userRateStatusString(state.userRate.status))
                    else -> Text(stringResource(id = R.string.feature_title_details_user_rate_add))
                }
            }
        },
        icon = {
            transition.AnimatedContent { state ->
                when (state) {
                    UserRateState.Loading -> CircularProgressIndicator()
                    is UserRateState.Success -> UserRateStatusIcon(status = state.userRate.status)
                    else -> AddIcon()
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
        contentDescription = userRateStatusString(userRateStatus = status)
    )
}