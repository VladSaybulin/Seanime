package ru.vladsaybulin.core.ui.filters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.vladsaybulin.model.search.FilterOption

enum class OptionValue {
    Selected, Unselected, Excluded
}

class FilterOptionState<T>(
    val option: FilterOption<T>,
    initialStateValue: OptionValue = OptionValue.Unselected
) {
    var value by mutableStateOf(initialStateValue)

    fun onClick() {
        value = when (value) {
            OptionValue.Selected, OptionValue.Excluded -> OptionValue.Unselected
            OptionValue.Unselected -> OptionValue.Selected
        }
    }

    fun onLongClick() {
        value = when (value) {
            OptionValue.Excluded -> OptionValue.Unselected
            OptionValue.Unselected,
            OptionValue.Selected -> OptionValue.Excluded
        }
    }
}