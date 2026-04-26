/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SearchBarDefaults.InputFieldHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.R
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeanimeSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = SeanimeSearchFieldDefaults.textFieldShape,
    placeholder: @Composable (() -> Unit)? = { SeanimeSearchFieldDefaults.Placeholder() },
    leadingIcon: @Composable (() -> Unit)? = { SeanimeSearchFieldDefaults.LeadingIcon() },
    trailingIcon: @Composable (() -> Unit)? = if (query.isNotEmpty()) {
        { SeanimeSearchFieldDefaults.TrailingIconButton { onQueryChange("") } }
    } else null,
    textFieldColors: TextFieldColors = SeanimeSearchFieldDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {


    val textColor = LocalTextStyle.current.color.takeOrElse {
        textFieldColors.textColor(enabled, isError = false, interactionSource = interactionSource).value
    }

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .layout { measurable, constraints ->
                val width = max(constraints.minWidth, SearchFieldMinWidth.roundToPx())
                    .coerceAtMost(min(constraints.maxWidth, SearchFieldMaxWidth.roundToPx()))
                val height = max(constraints.minHeight, InputFieldHeight.roundToPx())
                    .coerceAtMost(constraints.maxHeight)

                val placeable = measurable.measure(Constraints.fixed(width, height))
                layout(width, height) {
                    placeable.placeRelative(0, 0)
                }
            },
        textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
        enabled = enabled,
        singleLine = true,
        cursorBrush = SolidColor(textFieldColors.cursorColor),
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = enabled,
                visualTransformation = VisualTransformation.None,
                singleLine = true,
                interactionSource = interactionSource,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                shape = shape,
                colors = textFieldColors,
                container = {
                    Box(
                        modifier = Modifier
                            .background(
                                textFieldColors.containerColor(
                                    enabled = enabled,
                                    isError = false,
                                    interactionSource = interactionSource
                                ).value,
                                shape
                            )
                    )
                }
            )
        }
    )
}


object SeanimeSearchFieldDefaults {

    @Composable
    fun colors(
        focusedTextColor: Color = Color.Unspecified,
        unfocusedTextColor: Color = Color.Unspecified,
        disabledTextColor: Color = Color.Unspecified,
        focusedContainerColor: Color = SeanimeTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor: Color = SeanimeTheme.colorScheme.surfaceVariant,
        disabledContainerColor: Color = SeanimeTheme.colorScheme.surfaceVariant,
        cursorColor: Color = Color.Unspecified,
        errorCursorColor: Color = Color.Unspecified,
        selectionColors: TextSelectionColors? = null,
        focusedIndicatorColor: Color = Color.Transparent,
        unfocusedIndicatorColor: Color = Color.Transparent,
        disabledIndicatorColor: Color = Color.Transparent,
        focusedLeadingIconColor: Color = Color.Unspecified,
        unfocusedLeadingIconColor: Color = Color.Unspecified,
        disabledLeadingIconColor: Color = Color.Unspecified,
        focusedTrailingIconColor: Color = Color.Unspecified,
        unfocusedTrailingIconColor: Color = Color.Unspecified,
        disabledTrailingIconColor: Color = Color.Unspecified,
        focusedLabelColor: Color = Color.Unspecified,
        unfocusedLabelColor: Color = Color.Unspecified,
        disabledLabelColor: Color = Color.Unspecified,
        focusedPlaceholderColor: Color = Color.Unspecified,
        unfocusedPlaceholderColor: Color = Color.Unspecified,
        disabledPlaceholderColor: Color = Color.Unspecified,
        focusedSupportingTextColor: Color = Color.Unspecified,
        unfocusedSupportingTextColor: Color = Color.Unspecified,
        disabledSupportingTextColor: Color = Color.Unspecified,
        focusedPrefixColor: Color = Color.Unspecified,
        unfocusedPrefixColor: Color = Color.Unspecified,
        disabledPrefixColor: Color = Color.Unspecified,
        focusedSuffixColor: Color = Color.Unspecified,
        unfocusedSuffixColor: Color = Color.Unspecified,
        disabledSuffixColor: Color = Color.Unspecified,
    ) = TextFieldDefaults.colors(
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        errorTextColor = Color.Unspecified,
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        disabledContainerColor = disabledContainerColor,
        errorContainerColor = Color.Unspecified,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        selectionColors = selectionColors,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        errorIndicatorColor = Color.Unspecified,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        errorLeadingIconColor = Color.Unspecified,
        focusedTrailingIconColor = focusedTrailingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        errorTrailingIconColor = Color.Unspecified,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = Color.Unspecified,
        focusedPlaceholderColor = focusedPlaceholderColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        errorPlaceholderColor = Color.Unspecified,
        focusedSupportingTextColor = focusedSupportingTextColor,
        unfocusedSupportingTextColor = unfocusedSupportingTextColor,
        disabledSupportingTextColor = disabledSupportingTextColor,
        errorSupportingTextColor = Color.Unspecified,
        focusedPrefixColor = focusedPrefixColor,
        unfocusedPrefixColor = unfocusedPrefixColor,
        disabledPrefixColor = disabledPrefixColor,
        errorPrefixColor = Color.Unspecified,
        focusedSuffixColor = focusedSuffixColor,
        unfocusedSuffixColor = unfocusedSuffixColor,
        disabledSuffixColor = disabledSuffixColor,
        errorSuffixColor = Color.Unspecified
    )

    @Composable
    fun LeadingIcon() {
        Icon(
            imageVector = SeanimeIcons.Search,
            contentDescription = stringResource(id = R.string.core_designsystem_search_field_search)
        )
    }

    @Composable
    fun TrailingIconButton(onClick: () -> Unit) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = SeanimeIcons.Clear,
                contentDescription = stringResource(id = R.string.core_designsystem_search_field_clear)
            )
        }
    }

    @Composable
    fun Placeholder() {
        Text(text = stringResource(id = R.string.core_designsystem_search_field_search))
    }

    val textFieldShape = CircleShape
}

@Composable
internal fun TextFieldColors.containerColor(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()

    val targetValue = when {
        !enabled -> disabledContainerColor
        isError -> errorContainerColor
        focused -> focusedContainerColor
        else -> unfocusedContainerColor
    }
    return animateColorAsState(targetValue, tween(durationMillis = 300))
}

@Composable
internal fun TextFieldColors.textColor(
    enabled: Boolean,
    isError: Boolean,
    interactionSource: InteractionSource
): State<Color> {
    val focused by interactionSource.collectIsFocusedAsState()

    val targetValue = when {
        !enabled -> disabledTextColor
        isError -> errorTextColor
        focused -> focusedTextColor
        else -> unfocusedTextColor
    }
    return rememberUpdatedState(targetValue)
}

@Preview
@Composable
fun SeanimeSearchFieldPreview() {
    SeanimeTheme {
        Surface {
            SeanimeSearchField(
                query = "",
                onQueryChange = { }
            )
        }
    }
}

@Preview
@Composable
fun SeanimeSearchFieldWhenQueryIsNotEmptyPreview() {
    SeanimeTheme {
        Surface {
            SeanimeSearchField(
                query = "One piece",
                onQueryChange = { }
            )
        }
    }
}

@Preview(widthDp = 1000)
@Composable
fun SeanimeSearchFieldWhenMaxWidthPreview() {
    SeanimeTheme {
        Surface(
            modifier = Modifier
                .width(1000.dp)
                .height(56.dp)
        ) {
            SeanimeSearchField(
                query = "",
                onQueryChange = { },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private val SearchFieldMinWidth = 360.dp
private val SearchFieldMaxWidth = 720.dp
