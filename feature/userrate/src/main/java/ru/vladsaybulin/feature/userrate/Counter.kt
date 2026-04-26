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

package ru.vladsaybulin.feature.userrate

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection.Ltr
import androidx.compose.ui.unit.LayoutDirection.Rtl
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

class CounterColors(
    val containerColor: Color,
    val focusedContainerColor: Color,
    val disabledContainerColor: Color,
    val errorContainerColor: Color,
    val cursorColor: Color,
    val countColor: Color,
    val focusedCountColor: Color,
    val disabledCountColor: Color,
    val errorCountColor: Color,
    val labelColor: Color,
    val focusedLabelColor: Color,
    val disabledLabelColor: Color,
    val errorLabelColor: Color,
    val limitColor: Color,
    val focusedLimitColor: Color,
    val disabledLimitColor: Color,
    val errorLimitColor: Color,
    val buttonContainerColor: Color,
    val disabledButtonContainerColor: Color,
    val buttonContentColor: Color,
    val disabledButtonIconColor: Color
) {
    internal fun containerColor(enabled: Boolean, error: Boolean, isFocused: Boolean) = when {
        isFocused -> focusedContainerColor
        !enabled -> disabledContainerColor
        error -> errorContainerColor
        else -> containerColor
    }

    internal fun countColor(enabled: Boolean, error: Boolean, isFocused: Boolean) = when {
        isFocused -> focusedCountColor
        !enabled -> disabledCountColor
        error -> errorCountColor
        else -> countColor
    }

    internal fun labelColor(enabled: Boolean, error: Boolean, isFocused: Boolean) = when {
        isFocused -> focusedLabelColor
        !enabled -> disabledLabelColor
        error -> errorLabelColor
        else -> labelColor
    }

    internal fun limitColor(enabled: Boolean, error: Boolean, isFocused: Boolean) = when {
        isFocused -> focusedLimitColor
        !enabled -> disabledLimitColor
        error -> errorLimitColor
        else -> limitColor
    }

    internal fun iconButtonColors(): IconButtonColors = IconButtonColors(
        containerColor = buttonContainerColor,
        contentColor = buttonContentColor,
        disabledContainerColor = disabledButtonContainerColor,
        disabledContentColor = disabledButtonIconColor
    )
}

object CounterDefaults {

    @Composable
    fun colors(
        containerColor: Color = SeanimeTheme.colorScheme.outline,
        focusedContainerColor: Color = SeanimeTheme.colorScheme.primary,
        disabledContainerColor: Color = SeanimeTheme.colorScheme.outline
            .copy(alpha = DisabledContainerOpacity),
        errorContainerColor: Color = SeanimeTheme.colorScheme.error,
        cursorColor: Color = SeanimeTheme.colorScheme.primary,
        countColor: Color = SeanimeTheme.colorScheme.onSurface,
        focusedColor: Color = SeanimeTheme.colorScheme.onSurface,
        disabledCountColor: Color = SeanimeTheme.colorScheme.onSurface
            .copy(alpha = DisabledCountOpacity),
        errorCountColor: Color = SeanimeTheme.colorScheme.error,
        labelColor: Color = SeanimeTheme.colorScheme.onSurface,
        focusedLabelColor: Color = SeanimeTheme.colorScheme.primary,
        disabledLabelColor: Color = SeanimeTheme.colorScheme.onSurface
            .copy(alpha = DisabledLabelColorOpacity),
        errorLabelColor: Color = SeanimeTheme.colorScheme.error
            .copy(alpha = ErrorLabelColorOpacity),
        limitColor: Color = SeanimeTheme.colorScheme.onSurface
            .copy(alpha = LimitColorOpacity),
        focusedLimitColor: Color = SeanimeTheme.colorScheme.onSurface
            .copy(alpha = FocusedLimitColorOpacity),
        disabledLimitColor: Color = SeanimeTheme.colorScheme.onSurface
            .copy(alpha = DisabledLimitColorOpacity),
        errorLimitColor: Color = SeanimeTheme.colorScheme.onSurface
            .copy(alpha = ErrorLimitColorOpacity),
        buttonContainerColor: Color = Color.Transparent,
        buttonIconColor: Color = SeanimeTheme.colorScheme.onSecondaryContainer,
        disabledButtonContainerColor: Color = Color.Transparent,
        disabledButtonIconColor: Color = SeanimeTheme.colorScheme.onSecondaryContainer
            .copy(alpha = DisabledIconButtonOpacity)
    ) = CounterColors(
        containerColor = containerColor,
        focusedContainerColor = focusedContainerColor,
        disabledContainerColor = disabledContainerColor,
        errorContainerColor = errorContainerColor,
        cursorColor = cursorColor,
        countColor = countColor,
        focusedCountColor = focusedColor,
        disabledCountColor = disabledCountColor,
        errorCountColor = errorCountColor,
        labelColor = labelColor,
        focusedLabelColor = focusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        limitColor = limitColor,
        focusedLimitColor = focusedLimitColor,
        disabledLimitColor = disabledLimitColor,
        errorLimitColor = errorLimitColor,
        buttonContainerColor = buttonContainerColor,
        disabledButtonContainerColor = disabledButtonContainerColor,
        buttonContentColor = buttonIconColor,
        disabledButtonIconColor = disabledButtonIconColor
    )
}

@Composable
fun Counter(
    state: CounterState,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: (@Composable () -> Unit)? = null,
    limit: (@Composable () -> Unit)? = null,
    colors: CounterColors = CounterDefaults.colors(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isFocused = interactionSource.collectIsFocusedAsState().value

    val textStyle = SeanimeTheme.typography.bodyLarge.copy(
        color = colors.countColor(enabled, state.isError, isFocused),
        textAlign = TextAlign.Center
    )

    BasicTextField(
        value = state.textFieldValue,
        onValueChange = state::onTextFieldValueChanged,
        textStyle = textStyle,
        modifier = modifier.width(IntrinsicSize.Min),
        cursorBrush = SolidColor(colors.cursorColor),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            CounterDecorationBox(
                innerTextField = it,
                label = label,
                incrementButton = @Composable {
                    CounterButton(
                        icon = SeanimeIcons.Add,
                        contentDescription = stringResource(id = R.string.feature_user_rate_counter_increment),
                        enabled = { enabled && state.incrementEnabled },
                        onClick = state::onIncrement,
                        colors = colors.iconButtonColors()
                    )
                },
                decrementButton = @Composable {
                    CounterButton(
                        icon = SeanimeIcons.Remove,
                        contentDescription = stringResource(id = R.string.feature_user_rate_counter_decrement),
                        enabled = { enabled && state.decrementEnabled },
                        onClick = state::onDecrement,
                        colors = colors.iconButtonColors()
                    )
                },
                enabled = enabled,
                limit = limit,
                isError = state.isError,
                isFocused = isFocused,
                colors = colors,
                style = textStyle
            )
        },
        interactionSource = interactionSource
    )
}

@Composable
private fun CounterDecorationBox(
    style: TextStyle,
    innerTextField: @Composable () -> Unit,
    label: (@Composable () -> Unit)?,
    limit: (@Composable () -> Unit)?,
    incrementButton: @Composable () -> Unit,
    decrementButton: @Composable () -> Unit,
    enabled: Boolean,
    isError: Boolean,
    isFocused: Boolean,
    colors: CounterColors
) {
    val labelColor = colors.labelColor(enabled, isError, isFocused)
    val decoratedLabel: (@Composable () -> Unit)? = if (label != null) {
        @Composable {
            CounterDecoration(
                contentColor = labelColor,
                typography = SeanimeTheme.typography.bodySmall,
                content = label
            )
        }
    } else null

    val limitColor = colors.limitColor(enabled, isError, isFocused)
    val decoratedLimit: (@Composable () -> Unit)? = if (limit != null) {
        @Composable {
            CounterDecoration(
                contentColor = limitColor,
                typography = SeanimeTheme.typography.bodySmall.copy(fontSize = 9.sp),
                content = limit
            )
        }
    } else null

    CounterLayout(
        style = style,
        label = decoratedLabel,
        textField = innerTextField,
        incrementButton = incrementButton,
        decrementButton = decrementButton,
        limitText = decoratedLimit,
        container = {
            CounterContainer(color = colors.containerColor(enabled, isError, isFocused))
        }
    )
}

@Composable
private fun CounterContainer(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(16.dp)
            )
    )
}

@Composable
private fun CounterDecoration(
    contentColor: Color,
    typography: TextStyle? = null,
    content: @Composable () -> Unit
) {
    val contentWithColor: @Composable () -> Unit = @Composable {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            content = content
        )
    }
    if (typography != null)
        ProvideTextStyle(typography, contentWithColor)
    else
        contentWithColor()
}

@Composable
private fun CounterLayout(
    style: TextStyle,
    textField: @Composable () -> Unit,
    incrementButton: @Composable () -> Unit,
    decrementButton: @Composable () -> Unit,
    label: (@Composable () -> Unit)?,
    limitText: (@Composable () -> Unit)?,
    container: @Composable () -> Unit
) {
    val limitSize = remember {
        mutableStateOf(Size.Zero)
    }

    val labelSize = remember {
        mutableStateOf(Size.Zero)
    }

    val measurePolicy = remember {
        CounterMeasurePolicy(
            paddingValues = PaddingValues(0.dp),
            onLabelAndLimitMeasured = { newLabelSize, newLimitSize ->
                labelSize.value = newLabelSize
                limitSize.value = newLimitSize
            }
        )
    }

    Layout(
        content = {
            Box(
                modifier = Modifier
                    .layoutId(ContainerId)
                    .outlineCutout(labelSize.value, limitSize.value)
            ) {
                container()
            }

            Box(
                modifier = Modifier
                    .defaultMinSize(48.dp)
                    .layoutId(DecrementButtonId)
            ) {
                decrementButton()
            }

            Box(
                modifier = Modifier
                    .defaultMinSize(48.dp)
                    .layoutId(IncrementButtonId)
            ) {
                incrementButton()
            }

            Box(
                modifier = Modifier
                    .layoutId(TextFieldId)
                    .padding(horizontal = 8.dp)
                    .textFieldMinSize(style),
                contentAlignment = Alignment.Center
            ) {
                textField()
            }


            if (label != null) {
                Box(modifier = Modifier.layoutId(LabelId)) {
                    label()
                }
            }

            if (limitText != null) {
                Box(modifier = Modifier.layoutId(LimitId)) {
                    limitText()
                }
            }
        },
        measurePolicy = measurePolicy
    )
}

@Composable
private fun CounterButton(
    icon: ImageVector,
    contentDescription: String,
    enabled: () -> Boolean,
    onClick: () -> Unit,
    colors: IconButtonColors,
    modifier: Modifier = Modifier
) {
    val finalEnabled = enabled()
    IconButton(
        onClick = onClick,
        enabled = finalEnabled,
        modifier = modifier,
        colors = colors
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription
        )
    }
}

class CounterMeasurePolicy(
    private val paddingValues: PaddingValues,
    private val onLabelAndLimitMeasured: (labelSize: Size, limitSize: Size) -> Unit
) : MeasurePolicy {
    override fun MeasureScope.measure(
        measurables: List<Measurable>,
        constraints: Constraints
    ): MeasureResult {
        var occupiedSpaceHorizontally = 0

        val topPadding = paddingValues.calculateTopPadding().roundToPx()
        val startPadding = when (layoutDirection) {
            Ltr -> paddingValues.calculateLeftPadding(Ltr)
            Rtl -> paddingValues.calculateLeftPadding(Rtl)
        }.roundToPx()
        val bottomPadding = paddingValues.calculateTopPadding().roundToPx()
        val endPadding = when (layoutDirection) {
            Ltr -> paddingValues.calculateRightPadding(Ltr)
            Rtl -> paddingValues.calculateRightPadding(Rtl)
        }.roundToPx()

        val looseConstraints = constraints
            .copy(minWidth = 0, minHeight = 0)
            .offset(
                horizontal = -startPadding - endPadding,
                vertical = -topPadding - bottomPadding
            )

        //Measure decrement button
        val decrementButtonPlaceable = measurables.fastFirst { it.layoutId == DecrementButtonId }
            .measure(looseConstraints)
            .also { occupiedSpaceHorizontally += it.width }

        //Measure increment button
        val incrementButtonPlaceable = measurables.fastFirst { it.layoutId == IncrementButtonId }
            .measure(looseConstraints.offset(horizontal = -occupiedSpaceHorizontally))
            .also { occupiedSpaceHorizontally += it.width }


        var occupiedSpaceVertically = 0

        //Measure label
        val labelPlaceable = measurables.fastFirstOrNull { it.layoutId == LabelId }
            ?.measure(looseConstraints)
            ?.also { occupiedSpaceVertically += it.height }
        val labelSizeOrZero = labelPlaceable?.let { Size(it.width.toFloat(), it.height.toFloat()) }
            ?: Size.Zero

        //Measure limit
        val limitPlaceable = measurables.fastFirstOrNull { it.layoutId == LimitId }
            ?.measure(looseConstraints.offset(vertical = -occupiedSpaceVertically))
            ?.also { occupiedSpaceVertically += it.height }
        val limitSizeOrZero = limitPlaceable?.let { Size(it.width.toFloat(), it.height.toFloat()) }
            ?: Size.Zero

        onLabelAndLimitMeasured(labelSizeOrZero, limitSizeOrZero)

        //Measure text field
        val textFieldTopPadding = max(topPadding, heightOrZero(labelPlaceable))
        val textFieldBottomPadding = max(bottomPadding, heightOrZero(limitPlaceable))
        val textFieldConstraints = constraints.offset(
            horizontal = -occupiedSpaceHorizontally - startPadding - endPadding,
            vertical = -textFieldTopPadding - textFieldBottomPadding
        )
        val textFieldPlaceable = measurables.fastFirst { it.layoutId == TextFieldId }
            .measure(textFieldConstraints)

        val width = calculateWidth(
            incrementButtonWidth = incrementButtonPlaceable.width,
            decrementButtonWidth = decrementButtonPlaceable.width,
            labelWidth = widthOrZero(labelPlaceable),
            limitWidth = widthOrZero(limitPlaceable),
            textFieldWidth = textFieldPlaceable.width,
            constraints = constraints
        )

        val height = calculateHeight(
            decrementButtonHeight = decrementButtonPlaceable.height,
            incrementButtonHeight = incrementButtonPlaceable.height,
            labelHeight = heightOrZero(labelPlaceable),
            limitHeight = heightOrZero(limitPlaceable),
            textFieldHeight = textFieldPlaceable.height,
            paddingValues = paddingValues,
            density = density,
            constraints = looseConstraints
        )

        //Measure container
        val containerPlaceable = measurables.fastFirst { it.layoutId == ContainerId }
            .measure(Constraints.fixed(width, height))

        val totalHeight = height +
                heightOrZero(labelPlaceable) / 2 +
                heightOrZero(limitPlaceable) / 2 +
                (limitPlaceable?.let { LimitOffsetVertically.roundToPx() } ?: 0)

        return layout(width, totalHeight) {

            val containerTopPadding = heightOrZero(labelPlaceable) / 2
            val textFieldCenterY = containerTopPadding + height / 2

            containerPlaceable.place(
                x = 0,
                y = containerTopPadding
            )

            decrementButtonPlaceable.placeRelative(
                x = startPadding,
                y = textFieldCenterY - decrementButtonPlaceable.height / 2
            )

            incrementButtonPlaceable.placeRelative(
                x = width - endPadding - incrementButtonPlaceable.width,
                y = textFieldCenterY - incrementButtonPlaceable.height / 2
            )

            labelPlaceable?.place(
                x = (width - labelPlaceable.width) / 2,
                y = 0
            )

            limitPlaceable?.place(
                x = (width - limitPlaceable.width) / 2,
                y = totalHeight - limitPlaceable.height
            )

            textFieldPlaceable.place(
                x = (width - textFieldPlaceable.width) / 2,
                y = textFieldCenterY - textFieldPlaceable.height / 2
            )
        }
    }
}

fun calculateWidth(
    incrementButtonWidth: Int,
    decrementButtonWidth: Int,
    labelWidth: Int,
    limitWidth: Int,
    textFieldWidth: Int,
    constraints: Constraints
): Int {
    return max(
        constraints.minWidth,
        max(
            max(labelWidth, limitWidth),
            incrementButtonWidth + decrementButtonWidth + textFieldWidth
        )
    )
}

fun calculateHeight(
    decrementButtonHeight: Int,
    incrementButtonHeight: Int,
    labelHeight: Int,
    limitHeight: Int,
    textFieldHeight: Int,
    paddingValues: PaddingValues,
    density: Float,
    constraints: Constraints
): Int {

    val topPadding = (paddingValues.calculateTopPadding().value * density).roundToInt()
    val bottomPadding = (paddingValues.calculateTopPadding().value * density).roundToInt()

    val textFieldTopPadding = max(topPadding, labelHeight / 2)
    val textFieldBottomPadding = max(bottomPadding, limitHeight / 2)
    val middleSectionHeight = (textFieldTopPadding + textFieldHeight + textFieldBottomPadding)

    val buttonsHeight = max(incrementButtonHeight, decrementButtonHeight)
    println(middleSectionHeight)
    println(buttonsHeight + topPadding + bottomPadding)
    return max(
        constraints.minHeight,
        max(
            middleSectionHeight,
            buttonsHeight + topPadding + bottomPadding
        )
    )
}

private fun Modifier.outlineCutout(
    labelSize: Size,
    limitSize: Size
): Modifier = this.drawWithContent {
    val path = Path()

    val labelWidth = labelSize.width
    if (labelWidth != 0.0f) {
        val innerPadding = LabelPadding.toPx()
        val labelHeight = labelSize.height
        path.addRect(
            Rect(
                left = (size.width - labelWidth) / 2 - innerPadding,
                top = -labelHeight / 2,
                right = (size.width + labelWidth) / 2 + innerPadding,
                bottom = labelHeight / 2
            )
        )
    }

    val limitWidth = limitSize.width
    if (limitWidth != 0.0f) {
        val innerPadding = LimitPadding.toPx()
        val limitHeight = limitSize.height
        path.addRect(
            Rect(
                left = (size.width - limitWidth) / 2 - innerPadding,
                top = size.height - limitHeight / 2,
                right = (size.width + limitWidth) / 2 + innerPadding,
                bottom = size.height + limitHeight / 2
            )
        )
    }

    if (path.isEmpty) {
        drawContent()
    } else {
        clipPath(path = path, clipOp = ClipOp.Difference) {
            this@drawWithContent.drawContent()
        }
    }
}

@Composable
private fun Modifier.textFieldMinSize(style: TextStyle): Modifier {
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val minSize = remember(style, density, fontFamilyResolver) {
        computeSizeForDefaultText(
            style = style,
            density = density,
            fontFamilyResolver = fontFamilyResolver
        )
    }

    return LocalDensity.current.run {
        this@textFieldMinSize.sizeIn(
            minWidth = minSize.width.toDp(),
            minHeight = minSize.height.toDp()
        )
    }
}

private fun computeSizeForDefaultText(
    style: TextStyle,
    density: Density,
    fontFamilyResolver: FontFamily.Resolver,
    text: String = EmptyTextReplacement,
    maxLines: Int = 1
): IntSize {
    val paragraph = Paragraph(
        text = text,
        style = style,
        spanStyles = listOf(),
        maxLines = maxLines,
        ellipsis = false,
        density = density,
        fontFamilyResolver = fontFamilyResolver,
        constraints = Constraints()
    )
    return IntSize(
        ceil(paragraph.minIntrinsicWidth).roundToInt(),
        ceil(paragraph.height).roundToInt()
    )
}

@Preview
@Composable
fun TextFieldPreview() {
    Surface {
        OutlinedTextField(value = "1234", onValueChange = {}, label = { Text("Label") })
    }
}

@Preview
@Composable
fun SeanimeCounterPreview() {
    Surface {
        Counter(
            state = remember { CounterState(99999, 100000) },
            enabled = true,
        )
    }
}

@Preview
@Composable
fun SeanimeCounterWhenDisablePreview() {
    Surface {
        Counter(
            state = remember { CounterState(1, 48) },
            enabled = false
        )
    }
}

@Preview
@Composable
fun SeanimeCounterWithLabelPreview() {
    Surface {
        Counter(
            state = remember { CounterState(1, 48) },
            enabled = true,
            label = { Text("Label") }
        )
    }
}

@Preview
@Composable
fun SeanimeCounterWithLimitPreview() {
    Surface {
        Counter(
            state = remember { CounterState(1, 48) },
            enabled = true,
            limit = { Text("of 48") }
        )
    }
}

@Preview
@Composable
fun SeanimeCounterWithLabelAndLimitPreview() {
    Surface {
        FirstBaseline
        Counter(
            state = remember { CounterState(1, 48) },
            enabled = true,
            label = { Text("Label") },
            limit = { Text("of 48") }
        )
    }
}

@Preview
@Composable
fun SeanimeCounterWhenFocusedPreview() {
    val focusRequester = remember {
        FocusRequester()
    }

    LaunchedEffect(key1 = focusRequester) {
        focusRequester.requestFocus()
    }

    Surface {
        FirstBaseline
        Counter(
            state = remember { CounterState(1, 48) },
            enabled = true,
            modifier = Modifier.focusRequester(focusRequester)
        )
    }
}


@Preview
@Composable
fun SeanimeCounterWhenIsErrorPreview() {
    Surface {
        FirstBaseline
        Counter(
            state = remember { CounterState(54, 48) },
            enabled = true
        )
    }
}

private fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
private fun heightOrZero(placeable: Placeable?) = placeable?.height ?: 0

private const val EmptyTextReplacement = "99999"

private val LabelPadding = 4.dp
private val LimitPadding = 2.dp

private val LimitOffsetVertically = (-2).dp

private const val DisabledContainerOpacity = 0.5f
private const val DisabledCountOpacity = 0.5f
private const val DisabledLabelColorOpacity = 0.5f
private const val ErrorLabelColorOpacity = 0.5f
private const val DisabledLimitColorOpacity = 0.5f
private const val LimitColorOpacity = 0.5f
private const val FocusedLimitColorOpacity = 0.5f
private const val ErrorLimitColorOpacity = 0.5f
private const val DisabledIconButtonOpacity = 0.38f

private const val TextFieldId = "TextField"
private const val IncrementButtonId = "IncrementButton"
private const val DecrementButtonId = "DecrementButton"
private const val LabelId = "Label"
private const val LimitId = "LimitText"
private const val ContainerId = "Container"