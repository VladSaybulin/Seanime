package ru.vladsaybulin.feature.userrate.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

@Composable
private fun Counter(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange = 0..Int.MAX_VALUE,
    label: @Composable () -> Unit,
) {
    var isValueCorrect by remember { mutableStateOf(true) }
    var valueStr by remember(value) { mutableStateOf(value.toString()) }

    OutlinedTextField(
        value = valueStr,
        onValueChange = {
            valueStr = it
            try {
                val newValue = it.toInt()
                if (value == newValue || newValue in range) {
                    isValueCorrect = false
                }
                isValueCorrect = true
                onValueChange(newValue)
            } catch (_: Exception) {
                isValueCorrect = false
            }
        },
        leadingIcon = {
            IconButton(
                onClick = { onValueChange(value - 1) },
                enabled = value > range.first
            ) {
                Icon(
                    imageVector = ShikimoriIcons.Remove,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { onValueChange(value + 1) },
                enabled = value < range.last
            ) {
                Icon(
                    imageVector = ShikimoriIcons.Add,
                    contentDescription = null
                )
            }
        },
        isError = !isValueCorrect,
        label = label,
        keyboardActions = KeyboardActions(
            onDone = null
        ),
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        shape = CircleShape,
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.width(160.dp),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )
}

@Composable
@Preview
fun CounterPreview() {
    ShikimoriTheme {
        Surface {
            var value by remember { mutableIntStateOf(0) }

            Counter(
                value = value,
                onValueChange = { value = it },
                label = { Text("Episodes") }
            )
        }
    }
}