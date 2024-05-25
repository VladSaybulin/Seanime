package ru.vladsaybulin.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.colors.onUserRateStatusColor
import ru.vladsaybulin.core.ui.colors.userRateStatusColor
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun UserRateStatusBadge(
    modifier: Modifier = Modifier,
    userRateStatus: UserRateStatus,
    shape: Shape = UserRateStatusBadgeDefaults.topEndShape(),
) {
    val userRateStatusColor = userRateStatusColor(userRateStatus)
    val userRateIconColor = onUserRateStatusColor(userRateStatus)

    val icon = userRateStatusIcon(userRateStatus = userRateStatus) ?: return

    Box(
        modifier = modifier
            .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
            .clip(shape)
            .background(userRateStatusColor)
            .padding(DefaultIconPadding),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = userRateIconColor,
            modifier = Modifier.matchParentSize()
        )
    }
}

object UserRateStatusBadgeDefaults {

    @Composable
    fun topEndShape(from: CornerBasedShape = SeanimeTheme.shapes.medium) = from.copy(
        bottomEnd = ZeroCornerSize,
        topStart = ZeroCornerSize
    )
}


private val DefaultIconPadding = PaddingValues(4.dp)