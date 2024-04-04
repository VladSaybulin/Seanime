package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShikimoriModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    sheetContent: @Composable ColumnScope.() -> Unit
) {
    val topPaddingDp = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
            .padding(top = topPaddingDp)
            //Afterwards, the offset modifier will be applied inside the ModalBottomSheet.
            //This will cause the content to go down off the edge of the screen.
            //Therefore, we compensate for our indentation by shifting up
            .offset { IntOffset(x = 0, -topPaddingDp.toPx().roundToInt()) },
        content = sheetContent
    )
}