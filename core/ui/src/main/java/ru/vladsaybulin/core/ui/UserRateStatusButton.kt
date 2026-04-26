/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.designsystem.theme.get
import ru.vladsaybulin.core.ui2.strings.compose.asString
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun UserRateStatusButton(
    userRateStatus: UserRateStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = notNoneUserRateStatusIcon(userRateStatus),
    colors: UserRateStatusButtonColors = UserRateStatusButtonDefaults.userRateStatusButtonColors()
) {
    val transition = updateTransition(targetState = colors, label = "UserRateButtonColors")
    val animatedContainerColor by transition.animateColor(label = "Container") { targetColors ->
        targetColors.containerColor
    }
    val animatedContentColor by transition.animateColor(label = "Content") { targetColors ->
        targetColors.contentColor
    }

    Row(
        modifier = modifier
            .defaultMinSize(minWidth = MinWidth, minHeight = MinHeight)
            .clip(CircleShape)
            .background(animatedContainerColor)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(ContentPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val userRateText = userRateStatus.asString()
        Icon(
            imageVector = icon,
            contentDescription = userRateText,
            tint = animatedContentColor
        )
        Spacer(modifier = Modifier.width(IconSpacing))
        Text(
            text = userRateText,
            style = SeanimeTheme.typography.labelLarge,
            color = animatedContentColor
        )
    }
}

object UserRateStatusButtonDefaults {

    @Composable
    fun userRateStatusButtonColors(
        containerColor: Color = SeanimeTheme.colorScheme.surface.copy(alpha = 0f),
        contentColor: Color = SeanimeTheme.colorScheme.onSurface
    ) = UserRateStatusButtonColors(
        containerColor = containerColor,
        contentColor = contentColor
    )

    @Composable
    fun userRateStatusButtonColors(userRateStatus: UserRateStatus) =
        UserRateStatusButtonColors(
            containerColor = SeanimeTheme.seanimeColors[userRateStatus].container,
            contentColor = SeanimeTheme.seanimeColors[userRateStatus].onContainer
        )
}

data class UserRateStatusButtonColors(
    val containerColor: Color,
    val contentColor: Color
)

@Composable
@Preview
fun UserRateStatusButtonPreview() {
    SeanimeTheme {
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