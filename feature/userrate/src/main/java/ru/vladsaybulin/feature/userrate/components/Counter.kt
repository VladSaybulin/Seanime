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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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

@Stable
class CounterState(
    initialCount: Int,
    val range: IntRange
) {
    init {
        require(initialCount in range) { "initialCount out of range" }
    }

    var countStr by mutableStateOf(initialCount.toString())

    val countInt by derivedStateOf {
        countStr.toIntOrNull()?.takeIf { it in range }
    }

    val enabledIncrement by derivedStateOf {
        countInt.let { it != null && it < range.last }
    }

    val enabledDecrement by derivedStateOf {
        countInt.let { it != null && it > range.first }
    }

    val isError by derivedStateOf { countInt == null }

    fun increment() {
        val parsedCount = countInt ?: return
        countStr = (parsedCount + 1).toString()
    }

    fun decrement() {
        val parsedCount = countInt ?: return
        countStr = (parsedCount - 1).toString()
    }
}

@Composable
fun Counter(
    state: CounterState,
    modifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = state.countStr,
        onValueChange = { state.countStr = it },
        leadingIcon = {
            IconButton(
                onClick = { state.decrement() },
                enabled = state.enabledDecrement
            ) {
                Icon(
                    imageVector = ShikimoriIcons.Remove,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { state.increment() },
                enabled = state.enabledIncrement
            ) {
                Icon(
                    imageVector = ShikimoriIcons.Add,
                    contentDescription = null
                )
            }
        },
        isError = state.isError,
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
        modifier = modifier.width(160.dp),
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
    )
}

@Composable
@Preview
fun CounterPreview() {
    ShikimoriTheme {
        Surface {
            val state = remember { CounterState(initialCount = 0, range = 0..15) }

            Counter(
                state = state,
                label = { Text("Episodes") }
            )
        }
    }
}