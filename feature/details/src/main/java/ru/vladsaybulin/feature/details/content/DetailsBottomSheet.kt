package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun rememberDetailsBottomSheetState(
    initialShow: Boolean = false,
    scope: CoroutineScope = rememberCoroutineScope(),
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
) = remember(scope, sheetState) {
    DetailsBottomSheetState(
        initialShow = initialShow,
        scope = scope,
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
class DetailsBottomSheetState(
    initialShow: Boolean,
    val scope: CoroutineScope,
    val sheetState: SheetState,
) {
    private var _show = mutableStateOf(initialShow)

    val showSheet: Boolean
        get() = _show.value

    fun show() {
        _show.value = true
    }

    fun hide() {
        scope.launch { sheetState.hide() }
            .invokeOnCompletion { _show.value = false }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
    state: DetailsBottomSheetState,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    if (state.showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = state.sheetState,
            modifier = modifier,
            content = sheetContent
        )
    }
}