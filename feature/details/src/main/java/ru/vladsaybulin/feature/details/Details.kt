package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.model.EntryType

@Composable
fun DetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
    onRefresh: suspend () -> Unit,
    onEntryClick: (EntryType, Long) -> Unit
) {
    when (uiState) {
        is DetailsUiState.Error -> DetailsError(
            errorState = uiState,
            modifier = modifier,
            onRetry = onRetry
        )
        DetailsUiState.Loading -> {
            CircularProgressIndicator()
        }
        is DetailsUiState.Success -> {
            Text(text = uiState.details.originalName)
        }
    }
}

@Composable
private fun DetailsError(
    errorState: DetailsUiState.Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_message_title),
            style = ShikimoriTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.error_message),
            style = ShikimoriTheme.typography.bodyMedium
        )
        errorState.throwable.message?.let {
            Text(
                text = it,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = ShikimoriTheme.typography.bodySmall
            )
        }
        Button(onClick = onRetry) {
            Text(text = "Повторить")
        }
    }
}

@Composable
private fun DetailsLoading(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DetailsContent(
    state: DetailsUiState.Success,
    onEntryClick: (EntryType, Long) -> Unit,
    onRefresh: suspend () -> Unit,
    modifier: Modifier
) {

}