package ru.vladsaybulin.feature.character

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import ru.vladsaybulin.core.designsystem.components.ShikimoriCarousel
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.anime.AnimeCarousel
import ru.vladsaybulin.core.ui.text.SeanimeExpandableText
import ru.vladsaybulin.core.ui.text.onSeanimeTextLinkClickAdapter
import ru.vladsaybulin.core.ui.entry.EntryGridItem
import ru.vladsaybulin.core.ui.manga.MangaCarousel
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsNavEvents
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.character.CharacterDetails
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.core.ui.R as coreUiR

@Composable
fun CharacterDetailsScreen(
    navEvents: CharacterDetailsNavEvents,
    viewModel: CharacterDetailsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterDetailsScreen(
        uiState = uiState,
        onAnimeClick = navEvents.navigateToAnimeDetails,
        onMangaClick = navEvents.navigateToMangaDetails,
        onCharacterClick = navEvents.navigateToCharacterDetails,
        onPersonClick = navEvents.navigateToPersonDetails,
        onUrlClick = navEvents.navigateToUrl,
        onBack = navEvents.navigateUp
    )

}

@Composable
fun CharacterDetailsScreen(
    uiState: CharacterDetailsUiState,
    onAnimeClick: (id: Long) -> Unit,
    onMangaClick: (id: Long) -> Unit,
    onCharacterClick: (id: Long) -> Unit,
    onPersonClick: (id: Long) -> Unit,
    onUrlClick: (String) -> Unit,
    onBack: () -> Unit
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
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick,
                onCharacterClick = onCharacterClick,
                onPersonClick = onPersonClick,
                onUrlClick = onUrlClick,
                onBack = onBack
            )
        }
    }
}

@Composable
fun CharacterDetailsContent(
    uiState: CharacterDetailsUiState.Success,
    onAnimeClick: (id: Long) -> Unit,
    onMangaClick: (id: Long) -> Unit,
    onCharacterClick: (id: Long) -> Unit,
    onPersonClick: (id: Long) -> Unit,
    onUrlClick: (String) -> Unit,
    onBack: () -> Unit
) {
    val details = uiState.characterDetails
    LazyColumn(
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = CharacterLazyListItemKey.PosterAndNames) {
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
            item(key = CharacterLazyListItemKey.Description) {
                CharacterDescription(
                    description = details.description!!,
                    onAnimeClick = onAnimeClick,
                    onMangaClick = onMangaClick,
                    onCharacterClick = onCharacterClick,
                    onPersonClick = onPersonClick,
                    onUrlClick = onUrlClick
                )
            }
        }

        if (details.seyu.isNotEmpty()) {
            item(key = CharacterLazyListItemKey.Seyu) {
                Column {
                    Text(
                        text = stringResource(id = R.string.feature_character_seyu),
                        style = SeanimeTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    ShikimoriCarousel(items = details.seyu) {
                        EntryGridItem(
                            name = it.russianName ?: it.originalName,
                            imageUrl = it.poster?.originalUrl,
                            onClick = { },
                            modifier = Modifier.width(128.dp)
                        )
                    }
                }
            }
        }

        if (details.animes.isNotEmpty()) {
            item(key = CharacterLazyListItemKey.Animes) {
                Column {
                    Text(
                        text = stringResource(id = R.string.feature_character_animes),
                        style = SeanimeTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    AnimeCarousel(
                        anime = details.animes,
                        onClick = { onAnimeClick(it.id) }
                    )
                }
            }
        }

        if (details.mangas.isNotEmpty()) {
            item(key = CharacterLazyListItemKey.Mangas) {
                Column {
                    Text(
                        text = stringResource(id = R.string.feature_character_mangas),
                        style = SeanimeTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    MangaCarousel(
                        manga = details.mangas,
                        onClick = { onMangaClick(it.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterDescription(
    description: SeanimeText,
    onAnimeClick: (id: Long) -> Unit,
    onMangaClick: (id: Long) -> Unit,
    onCharacterClick: (id: Long) -> Unit,
    onPersonClick: (id: Long) -> Unit,
    onUrlClick: (String) -> Unit,
) {
    SeanimeExpandableText(
        text = description,
        style = SeanimeTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp),
        onLinkClick = onSeanimeTextLinkClickAdapter(
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick,
            onCharacterClick = onCharacterClick,
            onPersonClick = onPersonClick,
            onUrlClick = onUrlClick
        )
    )
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
            style = SeanimeTheme.typography.titleLarge,
            maxLines = 2
        )

        if (nameRu != null) {
            Text(
                text = name,
                style = SeanimeTheme.typography.bodyMedium,
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
            .clip(SeanimeTheme.shapes.medium)
    )
}

private enum class CharacterLazyListItemKey {
    PosterAndNames, Description, Seyu, Animes, Mangas
}

@Composable
@Preview
fun CharacterDetailsContentPreview() {
    SeanimeTheme {
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
                        description = SeanimeText(
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
                            styles = persistentListOf(),
                            inlineSpoilers = persistentListOf(),
                            spoilerBlocks = persistentListOf(),
                            links = persistentListOf()
                        ),
                        descriptionSource = null,
                        topicId = 31139,
                        updatedAt = Clock.System.now(),
                        seyu = emptyList(),
                        animes = emptyList(),
                        mangas = emptyList()
                    )
                ),
                onAnimeClick = { },
                onMangaClick = { },
                onCharacterClick = { },
                onPersonClick = { },
                onUrlClick = { },
                onBack = { }
            )
        }
    }
}

private val PosterWidth = 128.dp
private const val OtherNameTitleOpacity = 0.5f