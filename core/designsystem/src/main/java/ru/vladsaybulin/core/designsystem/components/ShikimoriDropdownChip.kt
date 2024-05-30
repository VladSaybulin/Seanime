package ru.vladsaybulin.core.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.util.fastForEach
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons

@Composable
fun <T> ShikimoriDropdownChip(
    items: List<T>,
    onItemClick: (T) -> Unit,
    selected: Boolean,
    selectedLabel: @Composable () -> Unit,
    itemLabel: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedLeadingIcon: (@Composable () -> Unit)? = null,
    itemLeadingIcon: (@Composable (T) -> Unit)? = null,
) {
    var expanded by remember { mutableStateOf(false) }

    val arrowRotation by animateFloatAsState(targetValue = if (expanded) 180f else 0f, label = "ArrowDropdownRotation")

    Box(modifier = modifier) {
        InputChip(
            selected = selected,
            onClick = { expanded = !expanded },
            label = {
                Box(modifier = Modifier.animateContentSize()) {
                    selectedLabel()
                }
            },
            enabled = enabled,
            leadingIcon = selectedLeadingIcon,
            trailingIcon = {
                Icon(
                    imageVector = SeanimeIcons.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(arrowRotation)
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.fastForEach { item ->
                DropdownMenuItem(
                    text = { itemLabel(item) },
                    onClick = {
                        onItemClick(item)
                        expanded = false
                    },
                    leadingIcon = if (itemLeadingIcon != null) {
                        { itemLeadingIcon(item) }
                    } else null
                )
            }
        }
    }
}