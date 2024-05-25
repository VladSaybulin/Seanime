package ru.vladsaybulin.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun ErrorMessageColumn(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = { ErrorMessageColumnDefaults.HeaderText() },
    description: (@Composable () -> Unit)? = null,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        val primaryColor = SeanimeTheme.colorScheme.primary
        val headerTextStyle = SeanimeTheme.typography.headlineMedium

        ProvideTextStyle(value = headerTextStyle.copy(color = primaryColor)) {
            header()
        }

        if (description != null) {
            ProvideTextStyle(value = SeanimeTheme.typography.bodyMedium) {
                description()
            }
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
            val primaryColor = SeanimeTheme.colorScheme.primary
            val headerTextStyle = SeanimeTheme.typography.headlineMedium

            ProvideTextStyle(value = headerTextStyle.copy(color = primaryColor)) {
                header()
            }

            ProvideTextStyle(value = SeanimeTheme.typography.bodyMedium) {
                description()
            }
        }

        if (action != null) {
            action()
        }
    }
}

object ErrorMessageColumnDefaults {

    @Composable
    fun HeaderText() {
        Text(text = stringResource(id = R.string.core_ui_error))
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
            style = SeanimeTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(id = R.string.core_ui_error_message),
            style = SeanimeTheme.typography.bodyMedium
        )
        throwable.message?.let {
            Text(
                text = it,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = SeanimeTheme.typography.bodySmall
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