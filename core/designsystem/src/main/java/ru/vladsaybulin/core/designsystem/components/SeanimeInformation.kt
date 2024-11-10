package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun SeanimeInformation(
    header: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    action: (@Composable () -> Unit)? = null,
    headerTextStyle: TextStyle = SeanimeInformationDefaults.headerTextStyle,
    descriptionTextStyle: TextStyle = SeanimeInformationDefaults.descriptionTextStyle
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon?.invoke()

        ProvideTextStyle(value = headerTextStyle, content = header)

        ProvideTextStyle(value = descriptionTextStyle, content = description)

        action?.invoke()
    }
}

object SeanimeInformationDefaults {
    val headerTextStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.headlineMedium.copy(color = DefaultHeaderColor)

    val descriptionTextStyle: TextStyle
        @Composable get() = SeanimeTheme.typography.bodyMedium
}

private val DefaultHeaderColor
    @Composable get() = SeanimeTheme.colorScheme.secondary