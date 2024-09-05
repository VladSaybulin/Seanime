package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Deprecated(
    message = "There is no need to use this component anymore as it used to fix WindowsInsets",
    replaceWith = ReplaceWith(
        expression = "ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = sheetState, modifier = modifier, content = sheetContent)",
        imports = ["import androidx.compose.material3.ModalBottomSheet"]
    ),
    level = DeprecationLevel.WARNING
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShikimoriModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier,
        content = sheetContent
    )
}