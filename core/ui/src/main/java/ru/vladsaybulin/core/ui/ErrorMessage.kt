package ru.vladsaybulin.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

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