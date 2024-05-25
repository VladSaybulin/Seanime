package ru.vladsaybulin.seanime.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.seanime.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequireAuthDialog(
    onSignIn: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = SeanimeTheme.shapes.extraLarge,
            color = SeanimeTheme.colorScheme.surfaceContainer,
            contentColor = SeanimeTheme.colorScheme.onSurface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.require_auth_label),
                    style = SeanimeTheme.typography.titleLarge,
                    color = SeanimeTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.require_auth_description),
                    style = SeanimeTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        onSignIn()
                        onDismissRequest()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.require_auth_sign_in))
                }
            }
        }
    }
}

@Composable
@Preview
fun RequireAuthDialogPreview() {
    SeanimeTheme(darkTheme = true) {
        RequireAuthDialog(
            onSignIn = { },
            onDismissRequest = { }
        )
    }
}