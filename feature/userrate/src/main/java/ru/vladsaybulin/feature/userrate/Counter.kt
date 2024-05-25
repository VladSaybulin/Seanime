package ru.vladsaybulin.feature.userrate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@JvmInline
value class CounterLimit(val value: Int) {
    companion object {
        val Unlimited = CounterLimit(9999)
    }
}

@Stable
class CounterState(
    initialCount: Int,
    val limit: CounterLimit,
    private val onChanged: (Int) -> Unit = {},
) {
    private val range = 0..limit.value

    var count by mutableIntStateOf(initialCount)

    private var _textFieldValue by mutableStateOf(TextFieldValue(text = initialCount.toString()))
    val textFieldValue: TextFieldValue
        get() = _textFieldValue

    val isError: Boolean
        get() = count !in range

    val decrementEnabled
        get() = count > 0

    val incrementEnabled
        get() = count < limit.value

    fun onChange(newTextFieldValue: TextFieldValue) {

        //If selection changed
        if (newTextFieldValue.text == textFieldValue.text) {
            _textFieldValue = newTextFieldValue
            return
        }

        //If text is blank then set zero
        if (newTextFieldValue.text.isBlank()) {
            _textFieldValue = TextFieldValue(
                text = "0",
                selection = TextRange(1)
            )
            updateCount(0)
            return
        }

        val newCount = newTextFieldValue.text.toIntOrNull() ?: return

        //If minus sign is entered
        if (newCount < 0) return

        updateCount(newCount, newTextFieldValue)
    }

    fun onIncrement() {
        if (!incrementEnabled) return
        updateCount(count + 1)
    }

    fun onDecrement() {
        if (!decrementEnabled) return
        updateCount(count - 1)
    }

    private fun updateCount(newCount: Int, fromTextFieldValue: TextFieldValue = _textFieldValue) {
        count = newCount
        _textFieldValue = fromTextFieldValue.copy(text = newCount.toString())
        onChanged(newCount)
    }
}

@Composable
fun Counter(
    state: CounterState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: @Composable () -> Unit
) {
    Layout(
        measurePolicy = CounterMeasurePolicy,
        modifier = modifier,
        content = {
            val contentColor = when {
                state.isError -> SeanimeTheme.colorScheme.error
                else -> LocalContentColor.current
            }
            CompositionLocalProvider(value = LocalContentColor provides contentColor) {
                DecrementButton(
                    onClick = state::onDecrement,
                    enabled = enabled && state.decrementEnabled,
                    modifier = Modifier.layoutId(CounterLayoutId.Decrement)
                )
                IncrementButton(
                    onClick = state::onIncrement,
                    enabled = enabled && state.incrementEnabled,
                    modifier = Modifier.layoutId(CounterLayoutId.Increment)
                )
                Box(modifier = Modifier.layoutId(CounterLayoutId.Label)) {
                    ProvideTextStyle(
                        value = SeanimeTheme.typography.labelMedium,
                        content = label
                    )
                }
                CounterTextField(
                    countStr = state.textFieldValue,
                    enabled = enabled,
                    onCounterStrChange = state::onChange,
                    modifier = Modifier.layoutId(CounterLayoutId.TextField)
                )
                if (state.limit != CounterLimit.Unlimited) {
                    Text(
                        text = stringResource(
                            id = R.string.feature_user_rate_out_of_limit,
                            state.limit.value
                        ),
                        style = SeanimeTheme.typography.labelSmall,
                        modifier = Modifier
                            .alpha(0.5f)
                            .layoutId(CounterLayoutId.Limit)
                    )
                }
            }
        }
    )
}

@Composable
private fun CounterTextField(
    countStr: TextFieldValue,
    enabled: Boolean,
    onCounterStrChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = countStr,
        onValueChange = onCounterStrChange,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        maxLines = 1,
        enabled = enabled,
        textStyle = SeanimeTheme.typography.headlineSmall.copy(
            textAlign = TextAlign.Center,
            color = LocalContentColor.current
        ),
        cursorBrush = SolidColor(SeanimeTheme.colorScheme.primary),
        modifier = modifier
    )
}

@Composable
private fun IncrementButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    FilledTonalIconButton(
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            imageVector = ShikimoriIcons.Add,
            contentDescription = stringResource(id = R.string.feature_user_rate_incement)
        )
    }
}

@Composable
private fun DecrementButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    FilledTonalIconButton(
        onClick = {
            if (enabled) {
                onClick()
            }
        },
        enabled = enabled,
        modifier = modifier
    ) {
        Icon(
            imageVector = ShikimoriIcons.Remove,
            contentDescription = stringResource(id = R.string.feature_user_rate_decrement)
        )
    }
}

enum class CounterLayoutId {
    TextField, Increment, Decrement, Label, Limit
}

object CounterMeasurePolicy : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {

        val measurablesConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val increment = measurables.first { it.layoutId == CounterLayoutId.Increment }
            .measure(measurablesConstraints)

        val decrement = measurables.first { it.layoutId == CounterLayoutId.Decrement }
            .measure(measurablesConstraints)

        val label = measurables.first { it.layoutId == CounterLayoutId.Label }
            .measure(measurablesConstraints)

        val limit = measurables.firstOrNull { it.layoutId == CounterLayoutId.Limit }
            ?.measure(measurablesConstraints)

        val minTextFieldWith = MinTextFieldWidth.roundToPx()
        val textFieldConstraints = Constraints.fixedWidth(label.width.coerceAtLeast(minTextFieldWith))
        val textField = measurables.first { it.layoutId == CounterLayoutId.TextField }
            .measure(textFieldConstraints)

        val width = increment.width + decrement.width + maxOf(
            label.width,
            textField.width,
            limit?.width ?: 0
        )

        val textFieldPos = IntOffset(
            x = decrement.width,
            y = label.height
        )
        val centerTextField = IntOffset(
            x = textFieldPos.x + textField.width / 2,
            y = textFieldPos.y + textField.height / 2
        )

        val incrementPos = IntOffset(
            x = width - increment.width,
            y = centerTextField.y - increment.height / 2
        )
        val decrementPos = IntOffset(
            x = 0,
            y = centerTextField.y - decrement.height / 2
        )

        val height = maxOf(
            a = textField.height + label.height + (limit?.height ?: 0),
            b = increment.height + incrementPos.y,
            c = decrement.height + decrementPos.y
        )

        return layout(width, height) {
            decrement.placeRelative(decrementPos)
            increment.placeRelative(incrementPos)

            label.placeRelative(x = centerTextField.x - label.width / 2, y = 0)
            textField.placeRelative(textFieldPos)
            limit?.placeRelative(
                x = centerTextField.x - limit.width / 2,
                y = label.height + textField.height
            )
        }
    }
}

@Composable
@Preview
fun CounterPreview() {
    SeanimeTheme {
        Surface {
            val state = remember {
                CounterState(initialCount = 0, limit = CounterLimit(15))
            }

            Counter(
                state = state,
                enabled = true,
                label = { Text("Эпизоды") }
            )
        }
    }
}

@Composable
@Preview
fun UnlimitedCounterPreview() {
    SeanimeTheme {
        Surface {
            val state = remember {
                CounterState(initialCount = 0, limit = CounterLimit.Unlimited)
            }

            Counter(
                state = state,
                enabled = true,
                label = { Text("Эпизоды") }
            )
        }
    }
}

@Composable
@Preview
fun IsErrorCounterPreview() {
    SeanimeTheme {
        Surface {
            val state = remember {
                CounterState(initialCount = 0, limit = CounterLimit(15))
            }

            LaunchedEffect(key1 = state) {
                state.onChange(TextFieldValue("16"))
            }

            Counter(
                state = state,
                enabled = true,
                label = { Text("Главы") }
            )
        }
    }
}

@Composable
@Preview
fun DisabledCounterPreview() {
    SeanimeTheme {
        Surface {
            val state = remember {
                CounterState(initialCount = 0, limit = CounterLimit.Unlimited)
            }

            Counter(
                state = state,
                enabled = false,
                label = { Text("Перепросм.") }
            )
        }
    }
}

private val MinTextFieldWidth = 96.dp