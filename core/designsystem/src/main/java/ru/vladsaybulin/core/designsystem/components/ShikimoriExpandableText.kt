package ru.vladsaybulin.core.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import ru.vladsaybulin.core.designsystem.R
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import kotlin.math.roundToInt

@Composable
fun ShikimoriExpandableText(
    text: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    collapsedLines: Int = 15,
    maxCollapsedLines: Int = collapsedLines,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    ExpandableTextLayout(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        firstTextLayoutResult = textLayoutResult,
        collapsedLines = collapsedLines,
        maxCollapsedLines = maxCollapsedLines,
        modifier = modifier
    ) { maxLines ->
        Text(
            text = text,
            modifier = textModifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = {
                if (textLayoutResult == null) {
                    textLayoutResult = it
                }
                onTextLayout(it)
            },
            style = style
        )
    }
}

@Composable
fun ShikimoriExpandableText(
    text: AnnotatedString,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    collapsedLines: Int = 10,
    maxCollapsedLines: Int = collapsedLines,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    var textLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }

    ExpandableTextLayout(
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        firstTextLayoutResult = textLayoutResult,
        collapsedLines = collapsedLines,
        maxCollapsedLines = maxCollapsedLines,
        modifier = modifier
    ) { maxLines ->
        Text(
            text = text,
            modifier = textModifier,
            color = color,
            fontSize = fontSize,
            fontStyle = fontStyle,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            letterSpacing = letterSpacing,
            textDecoration = textDecoration,
            textAlign = textAlign,
            lineHeight = lineHeight,
            overflow = overflow,
            softWrap = softWrap,
            maxLines = maxLines,
            onTextLayout = {
                if (textLayoutResult == null) {
                    textLayoutResult = it
                }
                onTextLayout(it)
            },
            style = style
        )
    }
}

@Composable
private fun ExpandableTextLayout(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    firstTextLayoutResult: TextLayoutResult?,
    modifier: Modifier,
    collapsedLines: Int,
    maxCollapsedLines: Int,
    content: @Composable (maxLines: Int) -> Unit,
) {
    var expandable by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = firstTextLayoutResult) {
        firstTextLayoutResult?.let {
            expandable = it.hasVisualOverflow
        }
    }

    val transition = updateTransition(targetState = expanded, label = "ExpandableText")
    val scrimOpacity by transition.animateFloat(label = "Scrim") { if (it) 0f else 1f }
    val arrowAnimationProgress by transition.animateFloat(label = "Arrow") { if (it) 1f else 0f }

    val scrimColor = SeanimeTheme.colorScheme.surface.copy(alpha = scrimOpacity)
    
    Layout(
        modifier = modifier,
        content = {
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .layoutId(ExpandableTextLayoutId.Text)
                    .then(
                        if (expandable) {
                            Modifier.drawForegroundGradientScrim(
                                topColor = scrimColor.copy(alpha = 0f),
                                bottomColor = scrimColor,
                            )
                        } else Modifier
                    )
            ) {
                content(
                    when {
                        firstTextLayoutResult == null -> maxCollapsedLines
                        !expandable || expanded -> Int.MAX_VALUE
                        else -> collapsedLines
                    }
                )
            }

            if (expandable) {
                IconButton(
                    onClick = { onExpandedChange(!expanded) },
                    modifier = Modifier.layoutId(ExpandableTextLayoutId.Arrow)
                ) {
                    Icon(
                        imageVector = SeanimeIcons.KeyboardArrowDown,
                        contentDescription = if (expanded) {
                            stringResource(id = R.string.core_designsystem_collapse_text)
                        } else {
                            stringResource(id = R.string.core_designsystem_expand_text)
                        },
                        modifier = Modifier.rotate(arrowAnimationProgress * 180f)
                    )
                }
            }
        },
        measurePolicy = { measurables, constraints ->

            val measureConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            val textPlaceable = measurables.first { it.layoutId == ExpandableTextLayoutId.Text }
                .measure(measureConstraints)

            val arrowPlaceable = measurables.firstOrNull { it.layoutId == ExpandableTextLayoutId.Arrow }
                ?.measure(measureConstraints)

            val endWidth = textPlaceable.width
            val endHeight = (textPlaceable.height +
                    (arrowAnimationProgress * 0.5 + 0.5) * (arrowPlaceable?.height ?: 0)).roundToInt()

            layout(endWidth, endHeight) {
                textPlaceable.place(x = 0, y = 0)
                arrowPlaceable?.place(
                    x = (endWidth - arrowPlaceable.width) / 2,
                    y = endHeight - arrowPlaceable.height
                )
            }
        }
    )
}

enum class ExpandableTextLayoutId {
    Text, Arrow
}

@Composable
@Preview
fun ShikimoriExpandableTextPreview() {
    SeanimeTheme {
        Surface {
            var expanded by remember { mutableStateOf(false) }

            ShikimoriExpandableText(
                text = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,",
                expanded = expanded,
                onExpandedChange = { expanded = it }
            )
        }
    }
}