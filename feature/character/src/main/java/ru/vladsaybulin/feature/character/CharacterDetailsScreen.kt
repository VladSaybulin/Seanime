package ru.vladsaybulin.feature.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import kotlinx.datetime.Clock
import ru.vladsaybulin.core.designsystem.components.ShikimoriExpandableText
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.navigation.NavigationEvent
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.toComposeAnnotatedString
import ru.vladsaybulin.model.annotatedtext.AnnotatedText
import ru.vladsaybulin.model.character.CharacterDetails
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.core.ui.R as coreUiR

@Composable
fun CharacterDetailsRoute(
    viewModel: CharacterDetailsViewModel = hiltViewModel(),
    onNavigationEvent: (NavigationEvent) -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterDetailsScreen(
        uiState = uiState,
        onNavigationEvent = onNavigationEvent
    )

}

@Composable
fun CharacterDetailsScreen(
    uiState: CharacterDetailsUiState,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(LocalScreenContentPadding.current)
            .statusBarsPadding()
            .fillMaxSize()
    ) {
        when (uiState) {
            is CharacterDetailsUiState.Error -> Unit
            CharacterDetailsUiState.Loading -> Unit
            is CharacterDetailsUiState.Success -> CharacterDetailsContent(
                uiState = uiState,
                onNavigationEvent = onNavigationEvent
            )
        }
    }
}

@Composable
fun CharacterDetailsContent(
    uiState: CharacterDetailsUiState.Success,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    val details = uiState.characterDetails
    LazyColumn(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CharacterDetailsPoster(poster = details.poster)
                CharacterDetailsNames(
                    name = details.name,
                    nameRu = details.nameRu,
                    nameJp = details.nameJp,
                    altNames = details.alternativeName
                )
            }
        }

        if (details.description != null) {
            item {
                CharacterDescription(details.description!!, onNavigationEvent)
            }
        }
    }
}

@Composable
fun CharacterDescription(
    description: AnnotatedText,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val layoutResult  = remember { mutableStateOf<TextLayoutResult?>(null) }

    val annotatedString = description.toComposeAnnotatedString()

    val pressIndicator = Modifier.pointerInput(Unit) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layoutResult ->
                val offset = layoutResult.getOffsetForPosition(pos)
                annotatedString.getStringAnnotations(offset, offset)
                    .fastForEach {
                        handleDescriptionClick(
                            tag = it.tag,
                            annotation = it.item,
                            onNavigationEvent = onNavigationEvent
                        )
                    }
            }
        }
    }

    ShikimoriExpandableText(
        text = annotatedString,
        expanded = expanded,
        onExpandedChange = setExpanded,
        style = ShikimoriTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp),
        textModifier = pressIndicator,
        onTextLayout = { layoutResult.value = it }
    )
}

fun handleDescriptionClick(
    tag: String,
    annotation: String,
    onNavigationEvent: (NavigationEvent) -> Unit
) {
    val navEventOrNull = when (tag) {
        "anime" -> NavigationEvent.EntryDetails(EntryType.Anime, annotation.toLong())
        "manga", "ranobe" -> NavigationEvent.EntryDetails(EntryType.Manga, annotation.toLong())
        "character", -> NavigationEvent.CharacterDetails(annotation.toLong())
        else -> null
    }
    if (navEventOrNull != null) {
        onNavigationEvent(navEventOrNull)
    }
}

@Composable
fun CharacterDetailsNames(
    name: String,
    nameRu: String?,
    nameJp: String?,
    altNames: String?
) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(
            text = nameRu ?: name,
            style = ShikimoriTheme.typography.titleLarge,
            maxLines = 2
        )

        if (nameRu != null) {
            Text(
                text = name,
                style = ShikimoriTheme.typography.bodyMedium,
                maxLines = 2
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        val nameColor = LocalContentColor.current
        val titleColor = nameColor.copy(alpha = OtherNameTitleOpacity)

        if (nameRu != null || nameJp != null || altNames != null) {
            Text(
                text = buildAnnotatedString {
                    if (nameJp != null) {
                        val title = stringResource(id = R.string.feature_character_japanese)
                        withStyle(SpanStyle(color = titleColor, fontSize = 0.75.em)) {
                            append(title)
                        }
                        append('\n').append(nameJp)
                    }

                    append('\n')

                    if (altNames != null) {
                        val title = stringResource(id = R.string.feature_character_alternative)
                        withStyle(SpanStyle(color = titleColor, fontSize = 0.75.em)) {
                            append(title)
                        }
                        append('\n').append(altNames)
                    }
                }
            )
        }
    }
}

@Composable
fun CharacterDetailsPoster(poster: Image?) {
    val painter = if (poster == null || LocalInspectionMode.current) {
        painterResource(id = coreUiR.drawable.no_poster)
    } else {
        rememberAsyncImagePainter(model = poster.originalUrl)
    }

    Image(
        painter = painter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(PosterWidth)
            .aspectRatio(3 / 4f)
            .clip(ShikimoriTheme.shapes.medium)
    )
}

@Composable
@Preview
fun CharacterDetailsContentPreview() {
    ShikimoriTheme {
        Surface {
            CharacterDetailsContent(
                uiState = CharacterDetailsUiState.Success(
                    CharacterDetails(
                        id = 40,
                        name = "Luffy Monkey D.",
                        nameRu = "Луффи Монки Д.",
                        poster = Image("", ""),
                        alternativeName = "Mugiwara, Straw Hat",
                        nameJp = "モンキー・D・ルフィ",
                        description = AnnotatedText(
                            text = """
                                Главный герой манги и аниме «Большой куш», является капитаном «Пиратов Соломенной Шляпы».

                                Внешность
                                Не особо высокий паренёк со стройным, но мускулистым телосложением. Волосы чёрные, коротко стриженные и взъерошенные; глаза широко распахнутые, с любопытством глядящие на мир; под левым глазом — тонкая полоска шрама. После атаки Акаину в битве при Маринфорде приобрёл ещё один огромный шрам в виде креста посередине груди. Вне критических ситуаций образ довершает ещё и улыбка «счастливого идиота» от уха до уха.

                                Одежда — красная (в арке «Триллер Барка» — оранжевая, в «Амазон Лили» — синяя, в «Импел Даун» — жёлтая) жилетка, лёгкие сандалии и голубые (иногда чёрные или красные) шорты до колена. Неизменная потёртая соломенная шляпа, подаренная ему Шанксом, которую он, в свою очередь, получил от Роджера, с красной лентой является единственной его материальной ценностью и сохраняет своё законное место на голове, либо за плечами. За лентой шляпы с Арабасты находилась библиокарта Портгаса Д. Эйса. После двухлетней тренировки у Луффи появился жёлтый пояс, а вместо жилетки — красная рубашка.

                                История
                                История умалчивает о том, как именно Луффи провёл первые годы своей жизни и познакомился с Шанксом. Однако факт остаётся фактом — красноволосый пират вдохновил Луффи на пиратскую судьбу и поиск Ван-Писа. И шрам под глазом Луффи напоминает ему о том, что в раннем детстве мальчик сам себя поранил и едва не вырезал себе глаз, пытаясь доказать Шанксу, что уже является мужчиной. Более того, однажды Шанкс спас жизнь маленького Луффи, пожертвовав ради этого левой рукой. Неудивительно, что уже в семь лет Луффи обрёл кумира, подарившего ему ту самую соломенную шляпу. Бедный дедушка-морпех Монки Д. Гарп подумать даже не мог, что нещадные тренировки на выживание в горах и джунглях, которые он устраивал для внука, разовьют недюжинную выносливость в будущем пирате.

                                Как только мальчику стукнуло 17, он погрузился в лодку, погрузил возле себя некое подобие пиратского флага и отправился в гордом одиночестве в открытый океан, нисколько не беспокоясь о том факте, что в случае чего он пойдёт ко дну топором. Впрочем, удача сдружилась с обнаглевшим парнем — настолько, что он в краткие сроки успел собрать пиратскую команду, получить себе корабль, проскочить на Гранд Лайн и к текущему моменту заработать немаленькую цену за свою голову.

                                Характер
                                Наивно-восторженный — когда нет особых проблем, по-глупому самоотверженный — когда под угрозой жизнь или свобода накама (товарищей). Несмотря на то, что зачастую Луффи ведёт себя более чем глупо, в ситуациях, требующих капитанского вмешательства, он принимает единственно правильные решения. Вообще у Луффи очень хорошо развита интуиция (к примеру, он всегда вычленяет среди противников наиболее сильного и выбирает его себе в оппоненты). Легко и непринуждённо реагирует на насмешки, практически не обозляясь. Настойчив до победного конца, уверен в себе и в команде. Да и просто — верит людям, верит в людей. Но при смертельной опасности своих товарищей или соломенной шляпы Шанкса он может и убить.

                                Привычки
                                У Луффи множество нелогичных привычек. Вне боя он не слушает окружающих, часто отвлекается и уходит от сути дела, также может ни с того ни с сего заснуть прямо посреди разговора. Если что-то задумал, то так и сделает, уровень безрассудности и странности желания его не волнует. Любит сидеть на носу корабля, поедая мясо в неограниченных количествах, распивать с накама неопределённые алкогольные напитки и искать дюжину с хвостиком приключений на свою соломенную шляпу.

                                Силы и способности
                                Большинство атак Луффи основаны на силе дьявольского фрукта. Так как в детстве он по неосторожности съел Гому-Гому, то стал «резиновым человеком». Может растягивать любую часть своего тела и отражать пули, ядра и прочие заряды огнестрельного оружия. После встречи с Магелланом в Импел Дауне ему достался иммунитет к ядам. За два года тренировок с Рэйли научился пользоваться всеми видами воли. После возвращения на архипелаг Сабаоди смог одолеть Пацифиста с одного удара.
                            """.trimIndent(),
                            annotations = emptyList()
                        ),
                        descriptionSource = null,
                        topicId = 31139,
                        updatedAt = Clock.System.now(),
                        seyu = emptyList(),
                        animes = emptyList(),
                        mangas = emptyList()
                    )
                ),
                onNavigationEvent = { }
            )
        }
    }
}

private val PosterWidth = 128.dp
private val OtherNameTitleOpacity = 0.5f