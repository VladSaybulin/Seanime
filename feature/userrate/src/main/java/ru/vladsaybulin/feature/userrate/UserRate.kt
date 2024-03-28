package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun UserRateBottomSheet(
    viewModel: UserRateViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {
    val uiState = rememberUserRateUiState(userRate = viewModel.requireOriginalUserRate)
}
