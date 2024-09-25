package ru.vladsaybulin.feature.userrate

import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

/**
 * The state that to control [ru.vladsaybulin.feature.userrate.Counter]
 * @param initialCount the initial count
 * @param limit the max value that count can take. Must be [CounterState.UNLIMITED_LIMIT] if counter is unlimited
 * It is recommended that this limit be less than 99999 for correct display
 */
@Stable
class CounterState(
    initialCount: Int,
    val limit: Int = UNLIMITED_LIMIT
) {
    companion object {
        const val UNLIMITED_LIMIT = -1
    }

    init {
        check(limit == -1 || limit >= 0) {
            "Limit must be not have negative value, except -1 when it unlimited"
        }
    }

    /**
     * Current state of count as Integer
     */
    private val _intValueState = mutableIntStateOf(initialCount)
    var value: Int
        get() = _intValueState.intValue
        set(value) { onIntValueChanged(value) }

    /**
     * Current state of count as TextFieldValue for save selection
     */
    private val _textFieldValueState = mutableStateOf(TextFieldValue(initialCount.toString()))
    internal val textFieldValue
        get() = _textFieldValueState.value

    /**
     * This value is true if current count <0 or >limit
     * Always false if limit is unlimited
     */
    val isError by derivedStateOf {
        if (limit == -1) false else _intValueState.intValue !in 0..limit
    }

    /**
     * This value is true if current count can be increment
     * Always true if limit is unlimited
     */
    val incrementEnabled by derivedStateOf {
        limit == -1 || _intValueState.intValue < limit
    }

    /**
     * This value is true if current count can be decrement
     */
    val decrementEnabled by derivedStateOf {
        _intValueState.intValue > 0
    }

    /**
     * Increment current count by 1 if [incrementEnabled] is true
     */
    fun onIncrement() {
        _intValueState.intValue.let { current ->
            if (limit != -1 && current == limit) return@let
            onIntValueChanged(current + 1)
        }
    }

    /**
     * Decrement current count by 1 if [decrementEnabled] is true
     */
    fun onDecrement() {
        _intValueState.intValue.let { current ->
            if (current == 0) return@let
            onIntValueChanged(current - 1)
        }
    }

    private fun onIntValueChanged(newIntCount: Int) {
        _intValueState.intValue = newIntCount
        _textFieldValueState.value = newIntCount.toString().let {
            TextFieldValue(it, TextRange(it.length))
        }
    }

    internal fun onTextFieldValueChanged(newTextFieldValue: TextFieldValue) {
        //If only selection or composition is changed set new textFieldValue
        if (newTextFieldValue.text == _textFieldValueState.value.text) {
            _textFieldValueState.value = newTextFieldValue
            return
        }

        //If new text is blank, set zero
        if (newTextFieldValue.text.isBlank()) {
            onIntValueChanged(0)
            return
        }

        //Try parse newTextFieldValue.text to int.
        //If parsing is failed then not ignore this newTextFieldValue
        //If before there was a zero and a highlight at the end, then create a new TextFieldValue
        //to avoid text like 0X
        //Otherwise we just replace the textFieldValueState and intCountState
        val newCount = newTextFieldValue.text.toIntOrNull() ?: return
        val createNewTextFieldValue = value == 0 && newTextFieldValue.selectionAtEnd()
        if (createNewTextFieldValue) {
            onIntValueChanged(newCount)
        } else {
            _intValueState.intValue = newCount
            _textFieldValueState.value = newTextFieldValue
        }
    }
}

private fun TextFieldValue.selectionAtEnd() =
    selection.length == 0 && selection.start == text.length