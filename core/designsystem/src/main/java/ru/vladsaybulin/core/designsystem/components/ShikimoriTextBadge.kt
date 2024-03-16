package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

@Composable
fun ShikimoriTextBadge(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.contentColorFor(containerColor),
    border: BorderStroke = TextBadgeDefaults.border(),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .border(border, shape)
            .padding(ChipContentPadding)
    ) {
        CompositionLocalProvider(value = LocalContentColor provides contentColor) {
            content()
        }
    }
}

object TextBadgeDefaults {

    @Composable
    fun border(
        width: Dp = DefaultBorderStrokeWidth,
        color: Color = MaterialTheme.colorScheme.outline
    ) = BorderStroke(width, color)
}

@Composable
@Preview
fun ShikimoriTextBadgePreview() {
    ShikimoriTheme {
        Surface {
            ShikimoriTextBadge(modifier = Modifier.padding(8.dp)) {
                Text(text = "Badge")
            }
        }
    }
}

private val DefaultBorderStrokeWidth = 1.dp
private val ChipContentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)