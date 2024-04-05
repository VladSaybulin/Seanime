package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
internal fun InfoLine(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    iconAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 2.dp)
    ) {
        Box(modifier = Modifier.align(iconAlignment)) {
            icon()
        }
        Spacer(modifier = Modifier.width(12.dp))
        ProvideTextStyle(value = MaterialTheme.typography.bodyMedium) {
            content()
        }
    }
}

@Composable
internal fun InfoIconPlaceholder(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.width(16.dp))
}

@Composable
internal fun InfoIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = null,
        modifier = modifier.size(16.dp)
    )
}

