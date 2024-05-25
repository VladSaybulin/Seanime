package ru.vladsaybulin.core.ui.annotatedtext

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.core.designsystem.components.drawForegroundGradientScrim
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.annotatedtext.SeanimeText


@Composable
fun SeanimeExpandableText(
    text: SeanimeText,
    onLinkClick: (tag: String, annotation: String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    val parts = if (text.spoilerBlocks.isEmpty()) {
        listOf(SeanimeTextPart.Regular(text))
    } else {
        remember(text) { text.splitToParts() }
    }

    val expanded = remember { mutableStateOf(false) }

    val expandedTransition = updateTransition(targetState = expanded.value, label = "Expanded")

    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }

    val textStyle = style.merge(
        color = textColor,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign ?: TextAlign.Unspecified,
        lineHeight = lineHeight,
        fontFamily = fontFamily,
        textDecoration = textDecoration,
        fontStyle = fontStyle,
        letterSpacing = letterSpacing
    )

    val scrim = @Composable {
        val animatedOpacity by expandedTransition.animateFloat(label = "scrimOpacity") { targetExpanded ->
            if (targetExpanded) 0f else 1f
        }
        Box(
            modifier = Modifier.drawForegroundGradientScrim(
                bottomColor = SeanimeTheme.colorScheme.surface.copy(animatedOpacity)
            )
        )
    }

    val expandCollapseButton = @Composable {
        val rotation by expandedTransition.animateFloat(label = "expandCollapseButtonRotation") { targetExpanded ->
            if (targetExpanded) 180f else 0f
        }

        IconButton(onClick = { expanded.value = !expanded.value }) {
            Icon(
                imageVector = ShikimoriIcons.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.graphicsLayer { rotationZ = rotation }
            )
        }
    }

    SeanimeExpandableTextLayout(
        parts = parts,
        expandedProvider = { expanded.value },
        scrim = scrim,
        expandCollapseButton = expandCollapseButton,
        modifier = modifier
    ) { part ->
        SeanimeTextPartContent(
            part = part,
            style = textStyle,
            onLinkClick = onLinkClick
        )
    }
}

@Composable
private fun SeanimeTextPartContent(
    part: SeanimeTextPart,
    style: TextStyle,
    onLinkClick: (tag: String, annotation: String) -> Unit
) {
    when (part) {
        is SeanimeTextPart.Regular -> TextContent(
            text = part.text,
            style = style,
            onLinkClick = onLinkClick
        )

        is SeanimeTextPart.SpoilerBlock -> SpoilerBlockContent(
            title = part.title,
            parts = part.parts,
            style = style,
            onLinkClick = onLinkClick
        )
    }
}

@Composable
private fun TextContent(
    text: SeanimeText,
    style: TextStyle,
    onLinkClick: (tag: String, annotation: String) -> Unit,
) {
    val annotatedString = text.toComposeAnnotatedString(SeanimeTheme.colorScheme.primary)

    var textLayout by remember { mutableStateOf<TextLayoutResult?>(null) }

    BasicText(
        text = annotatedString,
        style = style.copy(
            platformStyle = PlatformTextStyle(includeFontPadding = false),
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Center,
                trim = LineHeightStyle.Trim.Both
            )
        ),
        modifier = Modifier.then(
            if (textLayout != null) {
                Modifier
                    .inlineSpoilers(
                        textLayout = textLayout!!,
                        inlineSpoilers = text.inlineSpoilers,
                        inlineSpoilerColor = SeanimeTheme.colorScheme.secondaryContainer
                    )
                    .clickableLinks(
                        textLayout = textLayout!!,
                        links = text.links,
                        onClick = onLinkClick
                    )
            } else Modifier
        ),
        onTextLayout = { textLayout = it }
    )
}

@Composable
private fun SpoilerBlockContent(
    title: SeanimeText?,
    parts: List<SeanimeTextPart>,
    style: TextStyle,
    onLinkClick: (tag: String, annotation: String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .border(
                border = BorderStroke(width = 1.dp, color = SeanimeTheme.colorScheme.outline),
                shape = SpoilerShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { expanded.value = !expanded.value }
            )
    ) {
        Column(
            modifier = Modifier
                .animateContentSize()
                .padding(SpoilerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val titleStyle = style.copy(color = SeanimeTheme.colorScheme.primary)
            if (title != null) {
                TextContent(
                    text = title,
                    style = titleStyle,
                    onLinkClick = { _, _ -> expanded.value = !expanded.value },
                )
            } else {
                Text(
                    text = stringResource(id = R.string.core_ui_spoiler),
                    style = titleStyle
                )
            }

            if (expanded.value) {
                parts.fastForEach {
                    SeanimeTextPartContent(
                        part = it,
                        style = style,
                        onLinkClick = onLinkClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun SeanimeExpandableTextLayout(
    parts: List<SeanimeTextPart>,
    expandedProvider: () -> Boolean,
    modifier: Modifier = Modifier,
    scrim: @Composable () -> Unit,
    expandCollapseButton: @Composable () -> Unit,
    content: @Composable (SeanimeTextPart) -> Unit,
) {
    var expandable by remember {
        mutableStateOf<Boolean?>(null)
    }

    val finalExpanded by remember {
        derivedStateOf {
            val nonNullExpandable = expandable ?: return@derivedStateOf true
            !nonNullExpandable || expandedProvider()
        }
    }

    SubcomposeLayout(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .clipToBounds(),
        measurePolicy = { constraints ->

            val targetHeight = when {
                expandable == null -> MaxNonExpandableHeight.roundToPx()
                finalExpanded -> Int.MAX_VALUE
                else -> CollapsedHeight.roundToPx()
            }

            var contentHeight = 0

            val contentPlaceables = mutableListOf<Placeable>()
            var partIndex = 0
            while (targetHeight - contentHeight > 0 && partIndex < parts.size) {
                val part = parts[partIndex]
                val measurable = subcompose(partIndex) { content(part) }.single()

                ++partIndex

                val placeable = measurable.measure(
                    Constraints(
                        minWidth = 0,
                        maxWidth = constraints.minWidth,
                        minHeight = 0,
                        maxHeight = Constraints.Infinity
                    )
                )

                contentPlaceables.add(placeable)
                contentHeight += placeable.height
            }

            val textHeight = if (expandable == null) {
                val newExpandable = contentHeight > targetHeight
                expandable = newExpandable
                if (newExpandable) CollapsedHeight.roundToPx() else contentHeight
            } else if (targetHeight == Int.MAX_VALUE) {
                contentHeight
            } else {
                targetHeight
            }

            val scrimPlaceable: Placeable?
            val expandCollapseButtonPlaceable: Placeable?
            if (expandable != false) {
                expandCollapseButtonPlaceable = subcompose(
                    slotId = ExpandedSeanimeTextSlotId.ExpandCollapseButton,
                    content = expandCollapseButton
                ).single().measure(Constraints())

                scrimPlaceable = subcompose(
                    slotId = ExpandedSeanimeTextSlotId.Scrim,
                    content = scrim
                ).single().measure(
                    Constraints.fixed(
                        width = constraints.minWidth,
                        height = textHeight + expandCollapseButtonPlaceable.height
                    )
                )
            } else {
                expandCollapseButtonPlaceable = null
                scrimPlaceable = null
            }

            val finalWidth = constraints.minWidth
            val finalHeight = textHeight + (expandCollapseButtonPlaceable?.height ?: 0)

            layout(finalWidth, finalHeight) {
                var y = 0
                contentPlaceables.fastForEach { placeable ->
                    placeable.place(0, y)
                    y += placeable.height
                }
                scrimPlaceable?.place(0, 0)
                expandCollapseButtonPlaceable?.place(
                    x = (finalWidth - expandCollapseButtonPlaceable.width) / 2,
                    y = finalHeight - expandCollapseButtonPlaceable.height
                )
            }
        }
    )
}

private enum class ExpandedSeanimeTextSlotId {
    Scrim, ExpandCollapseButton
}

@Preview
@Composable
fun SeanimeExpandableTextPreview() {
    val text =
        """Больше известный под прозвищем «Охотник на Пиратов», Зоро — мечник «Пиратов Соломенной Шляпы». Второй по силе член экипажа. Груб и эмоционален. Совершенствовал свои навыки боя, путешествуя с Джонни и Ёсаку, парочкой друзей, и ловя пиратов. Некоторое время спустя попал в переделку с морскими дозорными, откуда его вытащил Луффи. После этого всё время находится с командой, побеждая всё новых и новых противников как вначале в Ист Блю, так потом и на Гранд Лайн.

Цель Зоро — стать самым искусным фехтовальщиком, чему он и посвятил свою жизнь, постоянно совершенствуя своё мастерство. Конечная цель Зоро — победа в схватке с человеком по прозвищу «Соколиные Глаза» Михоук, лучшим фехтовальщиком мира, который небезосновательно был назван одним из персонажей как «монстр среди монстров». В течение двухлетней разлуки, по совпадению, находился с Михоуком на одном острове и, переступив через свою гордость, попросил последнего его тренировать. После двухлетней тренировки без труда разрубил под водой огромный галеон.

ПрошлоеС малых лет вместе со многими другими местными детьми обучался боевым искусствам в додзё деревни Симоцуки. Как и все, он желал стать выдающимся мечником, но этого мальчику было мало — он хотел стать лучшим. И первой преградой на его пути стала дочь владельца додзё, Куина, которую он не мог победить, сколько бы раз не дрался против неё и сколько бы мечей не было в его руках. Со временем они начали общаться больше, Зоро узнал Куину лучше, и к соперничеству прибавилась ещё и дружба. В это время они и пообещали друг другу, что кто-то из них в будущем обязательно станет самым великим мечником во всём мире. Постепенно Зоро привык к трёхмечевому стилю, но как только он стал более-менее уверен в своих силах против Куины, та погибла глупой смертью — упав с лестницы. В одной из глав показано, как отец Луффи, Монки Д. Драгон, забирает кого-то смертельно раненного на своём корабле, на фоне тренировок Зоро. С тех пор Зоро придерживается их обещания и всеми силами старается приблизить будущее «самого великого мечника» к себе.

Внешность
Высокий парень со смуглой кожей и натренированным телом. Лицо пребывает чаще всего в четырёх состояниях: «сонный, не обращать внимания», «удивлённо таращить глаза», «достали до такой степени, что сейчас применю новую технику» или «довольная улыбка, перед которой поставили очередную пинту эля». Волосы короткие, зелёные, ёжиком, поэтому его иногда называют «голова-трава» или просто «маримо». Одежда — практически всегда носит с собой три меча. Одевается обычно в тёмные штаны, такого же цвета ботинки, белую майку и зелёный харамаки вокруг пояса. Если эта одежда всё ещё может варьироваться, то некоторые элементы остаются неизменными — три серьги в левом ухе и бандана, повязанная вокруг левой руки. Во время серьёзных схваток бандана надевается на голову. После двухлетней тренировки у него остался шрам через левый глаз. Лишился ли он самого глаза, или закрытый глаз — часть тренировки, пока неизвестно.
    """

    val styles = listOf<SeanimeText.Range<SeanimeText.Style>>(
        SeanimeText.Range(
            start = 2052,
            end = 2061,
            tag = "",
            item = SeanimeText.Style.ReadyStyle(SeanimeText.ReadyStyleValue.H3)
        ),
        SeanimeText.Range(
            start = 1016,
            end = 1023,
            tag = "",
            item = SeanimeText.Style.ReadyStyle(SeanimeText.ReadyStyleValue.Italic),
        )
    )

    val links = listOf(
        SeanimeText.Range(
            start = 191,
            end = 197,
            tag = "character",
            item = "4893"
        ),
        SeanimeText.Range(
            start = 200,
            end = 205,
            tag = "character",
            item = "4893"
        ),
        SeanimeText.Range(
            start = 322,
            end = 327,
            tag = "character",
            item = "40"
        ),
        SeanimeText.Range(
            start = 664,
            end = 670,
            tag = "character",
            item = "2064"
        ),
        SeanimeText.Range(
            start = 1289,
            end = 1294,
            tag = "character",
            item = "2126"
        ),
    )

    val spoilerBlocks = listOf(
        SeanimeText.Range(
            start = 1016,
            end = 2050,
            tag = "",
            item = SeanimeText.SpoilerBlockItem.Block,
        ),
        SeanimeText.Range(
            start = 1016,
            end = 1023,
            tag = "",
            item = SeanimeText.SpoilerBlockItem.Title,
        )
    )

    val inlineSpoilers = listOf(
        SeanimeText.Range(
            start = 5,
            end = 15,
            tag = "",
            item = Unit
        )
    )

    SeanimeTheme {
        Surface {
            Box(modifier = Modifier.verticalScroll(rememberScrollState())) {
                SeanimeExpandableText(
                    text = SeanimeText(
                        text = text,
                        styles = styles.toImmutableList(),
                        links = links.toImmutableList(),
                        inlineSpoilers = inlineSpoilers.toImmutableList(),
                        spoilerBlocks = spoilerBlocks.toImmutableList()
                    ),
                    onLinkClick = { _, _ -> },
                )
            }
        }
    }
}

private val SpoilerPadding = PaddingValues(8.dp)
private val SpoilerShape = RoundedCornerShape(8.dp)

private val MaxNonExpandableHeight = 250.dp
private val CollapsedHeight = 150.dp