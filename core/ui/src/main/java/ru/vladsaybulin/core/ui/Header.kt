package ru.vladsaybulin.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun ContentWithHeader(
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        header()
        content()
    }
}

@Composable
fun ContentWithClickableHeader(
    headerText: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    headerStyle: TextStyle = SeanimeTheme.typography.labelLarge,
    content: @Composable () -> Unit
) {
    ContentWithHeader(
        header = {
            Header(
                text = headerText,
                headerStyle = headerStyle,
                modifier = modifier.clickable(
                    onClick = onClick,
                    enabled = enabled,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
        },
        modifier = modifier,
        content = content
    )
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    headerStyle: TextStyle = SeanimeTheme.typography.titleMedium,
    text: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        ProvideTextStyle(value = headerStyle) {
            text()
        }
    }
}