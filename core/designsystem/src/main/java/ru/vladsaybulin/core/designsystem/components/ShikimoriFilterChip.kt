package ru.vladsaybulin.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastFirst
import androidx.compose.ui.util.fastFirstOrNull
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShikimoriFilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = FilterChipDefaults.shape,
    colors: ShikimoriSelectableChipColors = ShikimoriFilterChipDefaults.filterChipColors(),
    elevation: ShikimoriSelectableChipElevation? = ShikimoriFilterChipDefaults.filterChipElevation(),
    border: BorderStroke? = FilterChipDefaults.filterChipBorder(enabled, selected),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    Surface(
        modifier = modifier
            .semantics { role = Role.Checkbox }
            .combinedClickable(
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
                interactionSource = interactionSource,
                indication = LocalIndication.current
            ),
        shape = shape,
        color = colors.containerColor(enabled, selected).value,
        tonalElevation = elevation?.tonalElevation(enabled) ?: 0.dp,
        shadowElevation = elevation?.shadowElevation(enabled, interactionSource)?.value ?: 0.dp,
        border = border
    ) {
        ChipContent(
            label = label,
            labelTextStyle = ShikimoriTheme.typography.labelLarge,
            labelColor = colors.labelColor(enabled, selected),
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            leadingIconColor = colors.leadingIconContentColor(enabled, selected),
            trailingIconColor = colors.trailingIconContentColor(enabled, selected),
            minHeight = FilterChipMinHeight,
        )
    }
}


@Composable
private fun ChipContent(
    label: @Composable () -> Unit,
    labelTextStyle: TextStyle,
    labelColor: Color,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    leadingIconColor: Color,
    trailingIconColor: Color,
    minHeight: Dp
) {
    CompositionLocalProvider(
        LocalContentColor provides labelColor,
        LocalTextStyle provides labelTextStyle
    ) {
        Layout(
            modifier = Modifier
                .defaultMinSize(minHeight = minHeight)
                .padding(FilterChipPadding),
            content = {
                if (leadingIcon != null) {
                    Box(
                        modifier = Modifier
                            .layoutId(LeadingIconLayoutId),
                        contentAlignment = Alignment.Center,
                        content = {
                            CompositionLocalProvider(
                                LocalContentColor provides leadingIconColor,
                                content = leadingIcon
                            )
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .layoutId(LabelLayoutId)
                        .padding(HorizontalElementsPadding, 0.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    content = { label() }
                )
                if (trailingIcon != null) {
                    Box(
                        modifier = Modifier
                            .layoutId(TrailingIconLayoutId),
                        contentAlignment = Alignment.Center,
                        content = {
                            CompositionLocalProvider(
                                LocalContentColor provides trailingIconColor,
                                content = trailingIcon
                            )
                        }
                    )
                }
            }
        ) { measurables, constraints ->
            val leadingIconPlaceable: Placeable? =
                measurables.fastFirstOrNull { it.layoutId == LeadingIconLayoutId }
                    ?.measure(constraints.copy(minWidth = 0, minHeight = 0))
            val leadingIconWidth = widthOrZero(leadingIconPlaceable)
            val leadingIconHeight = heightOrZero(leadingIconPlaceable)

            val trailingIconPlaceable: Placeable? =
                measurables.fastFirstOrNull { it.layoutId == TrailingIconLayoutId }
                    ?.measure(constraints.copy(minWidth = 0, minHeight = 0))
            val trailingIconWidth = widthOrZero(trailingIconPlaceable)
            val trailingIconHeight = heightOrZero(trailingIconPlaceable)

            val labelPlaceable = measurables.fastFirst { it.layoutId == LabelLayoutId }
                .measure(
                    constraints.offset(horizontal = -(leadingIconWidth + trailingIconWidth))
                )

            val width = leadingIconWidth + labelPlaceable.width + trailingIconWidth
            val height = maxOf(leadingIconHeight, labelPlaceable.height, trailingIconHeight)

            layout(width, height) {
                leadingIconPlaceable?.placeRelative(
                    0,
                    Alignment.CenterVertically.align(leadingIconHeight, height)
                )
                labelPlaceable.placeRelative(leadingIconWidth, 0)
                trailingIconPlaceable?.placeRelative(
                    leadingIconWidth + labelPlaceable.width,
                    Alignment.CenterVertically.align(trailingIconHeight, height)
                )
            }
        }
    }
}

data class ShikimoriSelectableChipColors(
    private val containerColor: Color,
    private val labelColor: Color,
    private val leadingIconColor: Color,
    private val trailingIconColor: Color,
    private val disabledContainerColor: Color,
    private val disabledLabelColor: Color,
    private val disabledLeadingIconColor: Color,
    private val disabledTrailingIconColor: Color,
    private val selectedContainerColor: Color,
    private val disabledSelectedContainerColor: Color,
    private val selectedLabelColor: Color,
    private val selectedLeadingIconColor: Color,
    private val selectedTrailingIconColor: Color
) {

    /**
     * Represents the container color for this chip, depending on [enabled] and [selected].
     *
     * @param enabled whether the chip is enabled
     * @param selected whether the chip is selected
     */
    @Composable
    internal fun containerColor(enabled: Boolean, selected: Boolean): State<Color> {
        val target = when {
            !enabled -> if (selected) disabledSelectedContainerColor else disabledContainerColor
            !selected -> containerColor
            else -> selectedContainerColor
        }
        return rememberUpdatedState(target)
    }

    /**
     * Represents the label color for this chip, depending on [enabled] and [selected].
     *
     * @param enabled whether the chip is enabled
     * @param selected whether the chip is selected
     */
    internal fun labelColor(enabled: Boolean, selected: Boolean): Color {
        return when {
            !enabled -> disabledLabelColor
            !selected -> labelColor
            else -> selectedLabelColor
        }
    }

    /**
     * Represents the leading icon color for this chip, depending on [enabled] and [selected].
     *
     * @param enabled whether the chip is enabled
     * @param selected whether the chip is selected
     */
    internal fun leadingIconContentColor(enabled: Boolean, selected: Boolean): Color {
        return when {
            !enabled -> disabledLeadingIconColor
            !selected -> leadingIconColor
            else -> selectedLeadingIconColor
        }
    }

    /**
     * Represents the trailing icon color for this chip, depending on [enabled] and [selected].
     *
     * @param enabled whether the chip is enabled
     * @param selected whether the chip is selected
     */
    internal fun trailingIconContentColor(enabled: Boolean, selected: Boolean): Color {
        return when {
            !enabled -> disabledTrailingIconColor
            !selected -> trailingIconColor
            else -> selectedTrailingIconColor
        }
    }
}

object ShikimoriFilterChipDefaults {

    val shape
        @Composable get() = ShikimoriTheme.shapes.small

    @Composable
    fun filterChipColors(
        containerColor: Color = Color.Transparent,
        labelColor: Color = ShikimoriTheme.colorScheme.onSurfaceVariant,
        leadingIconColor: Color = ShikimoriTheme.colorScheme.primary,
        trailingIconColor: Color = ShikimoriTheme.colorScheme.onSurfaceVariant,
        disabledContainerColor: Color = Color.Transparent,
        disabledLabelColor: Color = ShikimoriTheme.colorScheme.onSurface
            .copy(alpha = DisabledOpacity),
        disabledLeadingIconColor: Color = ShikimoriTheme.colorScheme.onSurface
            .copy(alpha = DisabledOpacity),
        disabledTrailingIconColor: Color = ShikimoriTheme.colorScheme.onSurface
            .copy(alpha = DisabledOpacity),
        selectedContainerColor: Color = ShikimoriTheme.colorScheme.secondaryContainer,
        disabledSelectedContainerColor: Color = ShikimoriTheme.colorScheme.onSurface
            .copy(alpha = FlatDisabledSelectedContainerColor),
        selectedLabelColor: Color = ShikimoriTheme.colorScheme.onSecondaryContainer,
        selectedLeadingIconColor: Color = ShikimoriTheme.colorScheme.onSecondaryContainer,
        selectedTrailingIconColor: Color = ShikimoriTheme.colorScheme.onSecondaryContainer
    ) = ShikimoriSelectableChipColors(
        containerColor = containerColor,
        labelColor = labelColor,
        leadingIconColor = leadingIconColor,
        trailingIconColor = trailingIconColor,
        disabledContainerColor = disabledContainerColor,
        disabledLabelColor = disabledLabelColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        selectedContainerColor = selectedContainerColor,
        disabledSelectedContainerColor = disabledSelectedContainerColor,
        selectedLabelColor = selectedLabelColor,
        selectedLeadingIconColor = selectedLeadingIconColor,
        selectedTrailingIconColor = selectedTrailingIconColor
    )

    @Composable
    fun filterChipElevation(
        elevation: Dp = 0.dp,
        pressedElevation: Dp = 0.dp,
        focusedElevation: Dp = 0.dp,
        hoveredElevation: Dp = 1.dp,
        draggedElevation: Dp = 8.dp,
        disabledElevation: Dp = elevation
    ): ShikimoriSelectableChipElevation = ShikimoriSelectableChipElevation(
        elevation = elevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation,
        draggedElevation = draggedElevation,
        disabledElevation = disabledElevation
    )
}


/**
 * Represents the elevation used in a selectable chip in different states.
 *
 * Note that its [tonalElevation] implementation only depends on [elevation] and [disabledElevation]
 *
 * @param elevation the elevation used when the chip is enabled.
 * @param pressedElevation the elevation used when the chip is pressed.
 * @param focusedElevation the elevation used when the chip is focused
 * @param hoveredElevation the elevation used when the chip is hovered.
 * @param draggedElevation the elevation used when the chip is dragged
 * @param disabledElevation the elevation used when the chip is not enabled
 */
@Immutable
data class ShikimoriSelectableChipElevation(
    val elevation: Dp,
    val pressedElevation: Dp,
    val focusedElevation: Dp,
    val hoveredElevation: Dp,
    val draggedElevation: Dp,
    val disabledElevation: Dp
) {
    /**
     * Represents the tonal elevation used in a chip, depending on [enabled].
     *
     * Tonal elevation is used to apply a color shift to the surface to give the it higher emphasis.
     * When surface's color is [ColorScheme.surface], a higher elevation will result in a darker
     * color in light theme and lighter color in dark theme.
     *
     * See [shadowElevation] which controls the elevation of the shadow drawn around the Chip.
     *
     * @param enabled whether the chip is enabled
     */
    internal fun tonalElevation(enabled: Boolean): Dp {
        return if (enabled) elevation else disabledElevation
    }

    /**
     * Represents the shadow elevation used in a chip, depending on [enabled] and
     * [interactionSource].
     *
     * Shadow elevation is used to apply a shadow around the surface to give it higher emphasis.
     *
     * See [tonalElevation] which controls the elevation with a color shift to the surface.
     *
     * @param enabled whether the chip is enabled
     * @param interactionSource the [InteractionSource] for this chip
     */
    @Composable
    internal fun shadowElevation(
        enabled: Boolean,
        interactionSource: InteractionSource
    ): State<Dp> {
        return animateElevation(enabled = enabled, interactionSource = interactionSource)
    }

    @Composable
    private fun animateElevation(
        enabled: Boolean,
        interactionSource: InteractionSource
    ): State<Dp> {
        val interactions = remember { mutableStateListOf<Interaction>() }
        var lastInteraction by remember { mutableStateOf<Interaction?>(null) }
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                when (interaction) {
                    is HoverInteraction.Enter -> {
                        interactions.add(interaction)
                    }

                    is HoverInteraction.Exit -> {
                        interactions.remove(interaction.enter)
                    }

                    is FocusInteraction.Focus -> {
                        interactions.add(interaction)
                    }

                    is FocusInteraction.Unfocus -> {
                        interactions.remove(interaction.focus)
                    }

                    is PressInteraction.Press -> {
                        interactions.add(interaction)
                    }

                    is PressInteraction.Release -> {
                        interactions.remove(interaction.press)
                    }

                    is PressInteraction.Cancel -> {
                        interactions.remove(interaction.press)
                    }

                    is DragInteraction.Start -> {
                        interactions.add(interaction)
                    }

                    is DragInteraction.Stop -> {
                        interactions.remove(interaction.start)
                    }

                    is DragInteraction.Cancel -> {
                        interactions.remove(interaction.start)
                    }
                }
            }
        }

        val interaction = interactions.lastOrNull()

        val target = if (!enabled) {
            disabledElevation
        } else {
            when (interaction) {
                is PressInteraction.Press -> pressedElevation
                is HoverInteraction.Enter -> hoveredElevation
                is FocusInteraction.Focus -> focusedElevation
                is DragInteraction.Start -> draggedElevation
                else -> elevation
            }
        }

        return object : State<Dp> {
            override val value: Dp = target
        }
    }
}

internal fun widthOrZero(placeable: Placeable?) = placeable?.width ?: 0
internal fun heightOrZero(placeable: Placeable?) = placeable?.height ?: 0

private const val DisabledOpacity = 0.38f
private const val FlatDisabledSelectedContainerColor = 0.12f

private const val LeadingIconLayoutId = "leadingIcon"
private const val LabelLayoutId = "label"
private const val TrailingIconLayoutId = "trailingIcon"

private val HorizontalElementsPadding = 8.dp

private val FilterChipPadding = PaddingValues(horizontal = HorizontalElementsPadding)

private val FilterChipMinHeight = 32.dp