package ru.vladsaybulin.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

@Composable
fun ErrorMessageColumn(
    header: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        modifier = modifier.fillMaxWidth(0.8f).fillMaxHeight().padding(horizontal = 16.dp)
    ) {
        val primaryColor = ShikimoriTheme.colorScheme.primary
        val headerTextStyle = ShikimoriTheme.typography.headlineMedium

        ProvideTextStyle(value = headerTextStyle.copy(color = primaryColor)) {
            header()
        }

        ProvideTextStyle(value = ShikimoriTheme.typography.bodyMedium) {
            description()
        }

        if (action != null) {
            action()
        }
    }
}

@Composable
fun ErrorMessageRow(
    header: @Composable () -> Unit,
    description: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val primaryColor = ShikimoriTheme.colorScheme.primary
            val headerTextStyle = ShikimoriTheme.typography.headlineMedium

            ProvideTextStyle(value = headerTextStyle.copy(color = primaryColor)) {
                header()
            }

            ProvideTextStyle(value = ShikimoriTheme.typography.bodyMedium) {
                description()
            }
        }

        if (action != null) {
            action()
        }
    }
}

@Composable
fun ErrorMessage(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.core_ui_error_message_title),
            style = ShikimoriTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(id = R.string.core_ui_error_message),
            style = ShikimoriTheme.typography.bodyMedium
        )
        throwable.message?.let {
            Text(
                text = it,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = ShikimoriTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun FullScreenErrorMessage(
    throwable: Throwable,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ErrorMessage(throwable)
    }
}

private val ErrorMessageRowHeight = 64.dp