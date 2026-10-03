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

package ru.vladsaybulin.feature.title.details.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastMap
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.designsystem.components.SeanimeHeader
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.domain.titledetails.ExpandableListWrapper
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui2.entry.related.RelatedTitleItem
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.feature.title.details.impl.content.DetailsTopBar
import ru.vladsaybulin.feature.title.details.impl.content.PreviewInfoData
import ru.vladsaybulin.feature.title.details.impl.content.PreviewScoreStatistics
import ru.vladsaybulin.feature.title.details.impl.content.PreviewUserRateStatusStatistics
import ru.vladsaybulin.feature.title.details.impl.content.RequireAuthDialog
import ru.vladsaybulin.feature.title.details.impl.content.SimilarTitle
import ru.vladsaybulin.feature.title.details.impl.content.TitleAuthors
import ru.vladsaybulin.feature.title.details.impl.content.TitleCharacters
import ru.vladsaybulin.feature.title.details.impl.content.TitleDescription
import ru.vladsaybulin.feature.title.details.impl.content.TitleInfo
import ru.vladsaybulin.feature.title.details.impl.content.TitleName
import ru.vladsaybulin.feature.title.details.impl.content.TitlePoster
import ru.vladsaybulin.feature.title.details.impl.content.TitleScore
import ru.vladsaybulin.feature.title.details.impl.content.TitleScreenshots
import ru.vladsaybulin.feature.title.details.impl.content.TitleUserRateStatusDiagram
import ru.vladsaybulin.feature.title.details.impl.content.TitleVideos
import ru.vladsaybulin.feature.title.details.impl.content.UserRateFab
import ru.vladsaybulin.feature.title.details.impl.navigation.IdleNavigator
import ru.vladsaybulin.feature.title.details.impl.navigation.TitleDetailsNavigator
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.anime.VideoKind
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryStatus.Ongoing
import ru.vladsaybulin.model.common.EntryStatus.Released
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.person.Person
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.related.RelationType
import ru.vladsaybulin.model.search.SeasonOfYear
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.title.Title
import ru.vladsaybulin.model.title.TitleKind
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.toUserRateValues
import kotlin.time.Duration.Companion.days

@Composable
fun TitleDetailsScreen(
    viewModel: TitleDetailsViewModel,
    navigator: TitleDetailsNavigator
) {
    val headerState = viewModel.headerState.collectAsStateWithLifecycle()
    val infoState = viewModel.infoState.collectAsStateWithLifecycle()
    val relatedTitlesState = viewModel.relatedTitlesState.collectAsStateWithLifecycle()
    val authorsState = viewModel.authorsState.collectAsStateWithLifecycle()
    val charactersState = viewModel.charactersState.collectAsStateWithLifecycle()
    val similarState = viewModel.similarTitlesState.collectAsStateWithLifecycle()
    val animeMediaState = viewModel.animeMediaState.collectAsStateWithLifecycle()
    val userRateState = viewModel.userRateState.collectAsStateWithLifecycle()

    val (showRequireAuthDialog, setShowRequireAuthDialog) = remember { mutableStateOf(false) }

    ProvideTitleStringsByType(viewModel.titleType) {
        DetailsScreen(
            type = viewModel.titleType,
            headerState = headerState.value,
            readInfoState = { infoState.value },
            readRelatedState = { relatedTitlesState.value },
            readCharactersState = { charactersState.value },
            readAuthorsState = { authorsState.value },
            readSimilarState = { similarState.value },
            readAnimeMediaState = { animeMediaState.value },
            readUserRateState = { userRateState.value },
            navigator = navigator,
            onUserRateClick = {
                if (viewModel.isAuthenticated()) {
                    navigator.onRateClick(
                        it?.id,
                        it?.toUserRateValues(),
                        viewModel.buildUserRateContext()
                    )
                } else {
                    setShowRequireAuthDialog(true)
                }
            }
        )
    }

    if (showRequireAuthDialog) {
        RequireAuthDialog(
            authWithShikimori = {
                setShowRequireAuthDialog(false)
                viewModel.onLoginClick()
            },
            onDismissRequest = {
                setShowRequireAuthDialog(false)
            }
        )
    }
}

@Composable
fun DetailsScreen(
    type: EntryType,
    headerState: HeaderState,
    readInfoState: () -> InfoState,
    readRelatedState: () -> RelatedTitlesState,
    readCharactersState: () -> CharactersState,
    readAuthorsState: () -> AuthorsState,
    readSimilarState: () -> SimilarTitlesState,
    readAnimeMediaState: () -> AnimeMediaState,
    readUserRateState: () -> UserRateState,
    navigator: TitleDetailsNavigator,
    onUserRateClick: (UserRate?) -> Unit
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {
        when (headerState) {
            is TitleDetailsLoadState.Loading -> DetailsLoading()

            is TitleDetailsLoadState.Success -> DetailsContent(
                type = type,
                headerData = headerState.data,
                readInfoState = readInfoState,
                readRelatedState = readRelatedState,
                readCharactersState = readCharactersState,
                readAuthorsState = readAuthorsState,
                readSimilarState = readSimilarState,
                readAnimeMediaState = readAnimeMediaState,
                readUserRateState = readUserRateState,
                navigator = navigator,
                onUserRateClick = onUserRateClick
            )
        }
    }
}

@Composable
private fun DetailsLoading(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsContent(
    type: EntryType,
    headerData: HeaderData,
    readInfoState: () -> InfoState,
    readRelatedState: () -> RelatedTitlesState,
    readCharactersState: () -> CharactersState,
    readAuthorsState: () -> AuthorsState,
    readSimilarState: () -> SimilarTitlesState,
    readAnimeMediaState: () -> AnimeMediaState,
    readUserRateState: () -> UserRateState,
    navigator: TitleDetailsNavigator,
    onUserRateClick: (UserRate?) -> Unit
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    val visibleTopBar by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val expandedFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            DetailsTopBar(
                visibleTopBar = visibleTopBar,
                title = headerData.run { nameRu ?: name },
                onBackClick = navigator.onBackClick,
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        floatingActionButton = {
            UserRateFab(
                readUserRateState = readUserRateState,
                expanded = expandedFab,
                onClick = onUserRateClick
            )
        }
    ) { scaffoldPadding ->
        val uriHandler = LocalUriHandler.current

        LazyColumn(
            state = listState,
            //FAB padding
            contentPadding = PaddingValues(bottom = 56.dp + 32.dp)
        ) {
            headerContent(
                headerData = headerData,
                scaffoldPadding = scaffoldPadding,
                onPosterClick = navigator.onPosterClick
            )

            val infoData = (readInfoState() as? TitleDetailsLoadState.Success)
                ?.data ?: return@LazyColumn

            gutterSpacer()
            titleInfo(
                type = type,
                kind = infoData.kind,
                status = infoData.status,
                episodes = infoData.episodes,
                episodesAired = infoData.episodesAired,
                episodeDuration = infoData.episodeDuration,
                chapters = infoData.chapters,
                volumes = infoData.volumes,
                nextEpisodeAt = infoData.nextEpisodeAt,
                airedOn = infoData.airedOn,
                releasedOn = infoData.releasedOn,
                timePeriodAiring = infoData.season,
                rating = infoData.rating,
                studios = infoData.studios,
                publishers = infoData.publishers,
                genres = infoData.genres,
                onStudioClick = { navigator.onStudioClick(it.id) },
                onPublisherClick = { navigator.onPublisherClick(infoData.searchType(), it.id) },
                onGenreClick = { navigator.onGenreClick(infoData.searchType(), it.id) }
            )

            infoData.description?.takeIf { it.text.isNotEmpty() }
                ?.let { description ->
                    gutterSpacer()
                    titleDescription(
                        description = description,
                        onAnimeClick = navigator.onAnimeClick,
                        onMangaClick = navigator.onMangaClick,
                        onCharacterClick = navigator.onCharacterClick,
                        onPersonClick = navigator.onPersonClick,
                        onUrlClick = uriHandler::openUri
                    )
                }

            val authorsState = readAuthorsState()
            if (authorsState is TitleDetailsLoadState.Success && authorsState.data.visibleItems.isNotEmpty()) {
                gutterSpacer()
                titleAuthors(
                    authors = authorsState.data.visibleItems,
                    onAuthorClick = { navigator.onPersonClick(it.id) },
                    onMoreClick = navigator.onAllAuthorsClick
                )
            }


            if (infoData.score > 0f) {
                gutterSpacer()
                titleScore(
                    score = infoData.score,
                    stats = infoData.scoreStats
                )
            }

            if (infoData.statusStats.isNotEmpty()) {
                gutterSpacer()
                titleUserRateStatusDiagram(infoData.statusStats)
            }

            val relatedTitlesState = readRelatedState()
            if (relatedTitlesState is TitleDetailsLoadState.Success && relatedTitlesState.data.visibleItems.isNotEmpty()) {
                gutterSpacer()
                titleRelated(
                    visibleTitles = relatedTitlesState.data.visibleItems,
                    hasMore = relatedTitlesState.data.hasMore,
                    onTitleClick = navigator.onTitleClick,
                    onMoreClick = navigator.onAllRelatedClick
                )
            }

            val characters = readCharactersState()
            if (characters is TitleDetailsLoadState.Success && characters.data.visibleItems.isNotEmpty()) {
                gutterSpacer()
                titleCharacters(
                    characters = characters.data.visibleItems.fastMap { it.character },
                    onCharacterClick = { navigator.onCharacterClick(it.id) },
                    onMoreClick = navigator.onAllCharactersClick
                )
            }

            val mediaState = readAnimeMediaState()
            if (mediaState is TitleDetailsLoadState.Success) {
                val (screenshots, videos) = mediaState.data

                screenshots.takeIf { it.isNotEmpty() }?.let { screenshots ->
                    gutterSpacer()
                    titleScreenshots(
                        visibleScreenshots = screenshots.take(5),
                        hasMore = screenshots.size > 5,
                        onScreenshotClick = { index ->
                            navigator.onScreenshotClick(screenshots.fastMap { it.originalUrl }, index)
                        },
                        onMoreClick = navigator.onAllScreenshotsClick
                    )
                }

                videos.takeIf { it.isNotEmpty() }?.let { videos ->
                    gutterSpacer()
                    titleVideos(
                        visibleVideos = videos.take(5),
                        hasMore = videos.size > 5,
                        onVideoClick = { /* TODO navigate to video */ },
                        onMoreClick = navigator.onAllVideosClick
                    )
                }
            }

            val similarState = readSimilarState()
            if (similarState is TitleDetailsLoadState.Success && similarState.data.isNotEmpty()) {
                gutterSpacer()
                similarTitles(
                    titles = similarState.data,
                    onTitleClick = { type, id ->
                        when (type) {
                            EntryType.Anime -> navigator.onAnimeClick(id)
                            EntryType.Manga -> navigator.onMangaClick(id)
                        }
                    }
                )
            }
        }
    }
}

private fun LazyListScope.gutterSpacer() {
    item {
        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun LazyListScope.headerContent(
    headerData: HeaderData,
    scaffoldPadding: PaddingValues,
    onPosterClick: (String) -> Unit
): Unit = headerData.run {
    titlePoster(
        posterUrl = poster?.originalUrl,
        topSpace = scaffoldPadding.calculateTopPadding(),
        onClick = {
            poster?.let { onPosterClick(it.originalUrl) }
        }
    )

    gutterSpacer()
    titleName(
        name = name,
        russianName = nameRu
    )
}

private fun LazyListScope.titlePoster(
    posterUrl: String?,
    topSpace: Dp,
    onClick: () -> Unit
) {
    item(PosterKey) {
        TitlePoster(
            posterUrl = posterUrl,
            topSpace = topSpace,
            onClick = onClick
        )
    }
}

private fun LazyListScope.titleName(
    name: String,
    russianName: String?,
) {
    item(NameKey) {
        TitleName(name = name, russianName = russianName)
    }
}

private fun LazyListScope.titleInfo(
    type: EntryType,
    kind: TitleKind,
    status: EntryStatus,
    episodes: Int,
    episodesAired: Int,
    episodeDuration: Int,
    chapters: Int,
    volumes: Int,
    nextEpisodeAt: Instant?,
    airedOn: IncompleteDate?,
    releasedOn: IncompleteDate?,
    timePeriodAiring: TimePeriodAiring.Season?,
    rating: AnimeRating,
    studios: List<Studio>,
    publishers: List<Publisher>,
    genres: List<Genre>,
    onStudioClick: (Studio) -> Unit,
    onPublisherClick: (Publisher) -> Unit,
    onGenreClick: (Genre) -> Unit
) {
    item(InfoKey) {
        TitleInfo(
            type = type,
            kind = kind,
            status = status,
            episodes = episodes,
            episodesAired = episodesAired,
            episodeDuration = episodeDuration,
            chapters = chapters,
            volumes = volumes,
            nextEpisodeAt = nextEpisodeAt,
            airedOn = airedOn,
            releasedOn = releasedOn,
            season = timePeriodAiring,
            rating = rating,
            studios = studios,
            publishers = publishers,
            genres = genres,
            onStudioClick = onStudioClick,
            onPublisherClick = onPublisherClick,
            onGenreClick = onGenreClick
        )
    }
}

private fun LazyListScope.titleDescription(
    description: SeanimeText,
    onAnimeClick: (Long) -> Unit?,
    onMangaClick: (Long) -> Unit?,
    onCharacterClick: (Long) -> Unit?,
    onPersonClick: (Long) -> Unit?,
    onUrlClick: (String) -> Unit?,
) {
    item(key = DescriptionKey) {
        TitleDescription(
            description = description,
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick,
            onCharacterClick = onCharacterClick,
            onPersonClick = onPersonClick,
            onUrlClick = onUrlClick
        )
    }
}

private fun LazyListScope.titleAuthors(
    authors: List<PersonWithRoles>,
    onAuthorClick: (Person) -> Unit,
    onMoreClick: () -> Unit
) {
    clickableHeader(headerTextId = R.string.authors, onClick = onMoreClick)

    item {
        TitleAuthors(
            authors = authors,
            onAuthorClick = onAuthorClick
        )
    }
}

private fun LazyListScope.titleScore(
    score: Float,
    stats: List<StatisticsItem<Int>>
) {
    header(R.string.feature_title_details_title_score)
    item {
        TitleScore(score = score, stats = stats)
    }
}

private fun LazyListScope.titleUserRateStatusDiagram(statisticItems: List<StatisticsItem<UserRateStatus>>) {
    header(R.string.feature_details_user_statuses)

    item {
        TitleUserRateStatusDiagram(statisticItems)
    }
}

private fun LazyListScope.titleRelated(
    visibleTitles: List<RelatedTitle>,
    hasMore: Boolean,
    onTitleClick: (Title) -> Unit,
    onMoreClick: () -> Unit
) {
    sectionHeaderWithMore(
        hasMore = hasMore,
        headerTextId = R.string.related,
        onMoreClick = onMoreClick
    )

    items(items = visibleTitles) { title ->
        RelatedTitleItem(
            relatedTitle = title,
            onClick = onTitleClick,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
        )
    }
}

private fun LazyListScope.titleCharacters(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit,
    onMoreClick: () -> Unit
) {
    clickableHeader(
        headerTextId = R.string.characters,
        onClick = onMoreClick
    )

    item {
        TitleCharacters(
            characters = characters,
            onCharacterClick = onCharacterClick
        )
    }
}

private fun LazyListScope.titleScreenshots(
    visibleScreenshots: List<Image>,
    hasMore: Boolean,
    onScreenshotClick: (index: Int) -> Unit,
    onMoreClick: () -> Unit
) {
    sectionHeaderWithMore(
        hasMore = hasMore,
        headerTextId = R.string.screenshots,
        onMoreClick = onMoreClick
    )

    item {
        TitleScreenshots(
            screenshots = visibleScreenshots,
            onScreenshotClick = onScreenshotClick
        )
    }
}


private fun LazyListScope.titleVideos(
    visibleVideos: List<Video>,
    hasMore: Boolean,
    onVideoClick: (Video) -> Unit,
    onMoreClick: () -> Unit
) {
    sectionHeaderWithMore(
        hasMore = hasMore,
        headerTextId = R.string.videos,
        onMoreClick = onMoreClick
    )

    item {
        TitleVideos(
            videos = visibleVideos,
            onVideoClick = onVideoClick
        )
    }
}

private fun LazyListScope.similarTitles(
    titles: List<Title>,
    onTitleClick: (EntryType, Long) -> Unit
) {
    header(headerTextId = R.string.similar)

    item {
        SimilarTitle(
            titles = titles,
            onTitleClick = { onTitleClick(it.type, it.id) }
        )
    }
}

private fun LazyListScope.header(headerTextId: Int) {
    item {
        SeanimeHeader {
            Text(stringResource(id = headerTextId))
        }
    }
}

private fun LazyListScope.clickableHeader(
    headerTextId: Int,
    onClick: () -> Unit
) {
    item {
        SeanimeHeader(
            modifier = Modifier.clickable(onClick = onClick),
            trailing = {
                Icon(
                    imageVector = SeanimeIcons.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        ) { Text(text = stringResource(id = headerTextId)) }
    }
}

private fun LazyListScope.sectionHeaderWithMore(
    hasMore: Boolean,
    headerTextId: Int,
    onMoreClick: () -> Unit
) {
    if (hasMore) {
        clickableHeader(headerTextId, onMoreClick)
    } else {
        header(headerTextId)
    }
}

@Composable
@Preview
fun EntryDetailsScreenPreview() {
    val header = HeaderData(
        poster = Image("", ""),
        name = "One Piece",
        nameRu = "Ван-Пис"
    ).asSuccess()


    val info = InfoData(
        titleType = EntryType.Anime,
        kind = TitleKind.Tv,
        status = Ongoing,
        episodes = 0,
        episodesAired = 1114,
        episodeDuration = 23,
        chapters = 0,
        volumes = 0,
        nextEpisodeAt = Clock.System.now() + 1.days,
        airedOn = IncompleteDate(day = 11, month = 8, year = 1999),
        releasedOn = null,
        season = TimePeriodAiring.Season(SeasonOfYear.Fall, 1999),
        rating = AnimeRating.PG13,
        studios = persistentListOf(Studio(1, "Toei Animation", imageUrl = null)),
        publishers = emptyList(),
        genres = persistentListOf(
            Genre(
                id = 1,
                englishName = "Senen",
                russianName = "Сёнен",
                entryType = EntryType.Anime,
                kind = GenreKind.Demographic
            ),
            Genre(
                id = 2,
                englishName = "Action",
                russianName = "Экшен",
                entryType = EntryType.Anime,
                kind = GenreKind.Genre
            ),
            Genre(
                id = 3,
                englishName = "Adventure",
                russianName = "Приключения",
                entryType = EntryType.Anime,
                kind = GenreKind.Genre
            ),
            Genre(
                id = 4,
                englishName = "Fantasy",
                russianName = "Фэнтези",
                entryType = EntryType.Anime,
                kind = GenreKind.Genre
            ),
        ),
        score = 8.82f,
        description = SeanimeText(
            text = """
                    Легендарный Гол Д. Роджер был пиратским королём, он был единственным пиратом, проплывшим Гранд Лайн от начала и до конца. Захват Роджера 22 года тому назад всемирным правительством привёл к изменениям во всём мире. Последние слова пирата перед казнью открыли расположение величайшего сокровища мира Ван-Пис. Тот, кто добудет его, станет новым Королём пиратов, и именно это событие положило начало Великой эры пиратов.
                    Монки Д. Луффи, 17-летний парень, бросает вызов Гранд Лайн. Он собирает команду и отправляется на поиски сокровища, мечтая о захватывающих приключениях и имея свои причины стать пиратом. Следуя по стопам своего героя детства, Короля пиратов, Луффи и его команда путешествуют по линии Великого моря навстречу безумным приключениям, сильным врагам, и всё для того, чтобы добыть великое сокровище мира — Ван-Пис.
                """.trimIndent(),
            styles = persistentListOf(),
            inlineSpoilers = persistentListOf(),
            spoilerBlocks = persistentListOf(),
            links = persistentListOf()
        ),
        descriptionSource = null,
        scoreStats = PreviewScoreStatistics,
        statusStats = PreviewUserRateStatusStatistics
    ).asSuccess()

    val relatedTitles = ExpandableListWrapper<RelatedTitle>(
        listOf(
            RelatedTitle(
                title = Title(
                    id = 813,
                    type = EntryType.Anime,
                    name = "Dragon Ball Z",
                    nameRu = "Драконий жемчуг Зет",
                    poster = null,
                    kind = TitleKind.Tv,
                    status = Released,
                    score = 8.18f,
                    episodes = 291,
                    episodesAired = 1,
                    chapters = 0,
                    volumes = 0,
                    airedOn = IncompleteDate(26, 4, 1989),
                    releasedOn = IncompleteDate(31, 1, 1996),

                    ),
                relationType = RelationType.Character
            ),
            RelatedTitle(
                title = Title(
                    id = 13,
                    type = EntryType.Manga,
                    name = "One Piece",
                    nameRu = "Ван пис",
                    poster = null,
                    kind = TitleKind.Manga,
                    status = Ongoing,
                    score = 9.22f,
                    episodes = 0,
                    episodesAired = 0,
                    chapters = 0,
                    volumes = 0,
                    airedOn = IncompleteDate(22, 7, 1997),
                    releasedOn = null
                ),
                relationType = RelationType.Adaptation
            )
        ),
        threshold = 2
    ).let(::ExpandableSectionState).asSuccess()

    val authors = ExpandableListWrapper(
        listOf(
            PersonWithRoles(
                person = Person(
                    id = 1,
                    originalName = "Echiro Oda",
                    russianName = "Эйтиро Ода",
                    poster = null
                ),
                roles = listOf("Original Creator")
            ),
            PersonWithRoles(
                person = Person(
                    id = 1,
                    originalName = "Miki Komuro",
                    russianName = "Мики Комуро",
                    poster = null
                ),
                roles = listOf("Original Creator")
            )
        ),
        threshold = 1
    ).let(::ExpandableSectionState).asSuccess()

    val characters = ExpandableListWrapper(
        listOf(
            CharacterWithRole(
                Character(
                    id = 40,
                    originalName = "Luffy Monkey D.",
                    russianName = "Луффи Монки Д.",
                    poster = null
                ),
                isMainRole = true
            ),
            CharacterWithRole(
                Character(
                    id = 62,
                    originalName = "Zoro Roronoa",
                    russianName = "Зоро Ророноа",
                    poster = null
                ),
                isMainRole = false
            ),
            CharacterWithRole(
                Character(
                    id = 62,
                    originalName = "Nami",
                    russianName = "Нами",
                    poster = null
                ),
                isMainRole = false
            )
        ),
        threshold = 2
    ).let(::ExpandableSectionState).asSuccess()

    val media = AnimeMediaData(
        screenshots = List(5) { Image("", "") },
        videos = List(5) { Video("", "", "", "", VideoKind.entries.random()) }
    ).asSuccess()

    val similar = emptyList<Title>().asSuccess()

    val userRate = (null as UserRate?).asSuccess()

    DetailsContent(
        type = EntryType.Anime,
        headerData = header.data,
        readInfoState = { info },
        readRelatedState = { relatedTitles },
        readCharactersState = { characters },
        readAuthorsState = { authors },
        readSimilarState = { similar },
        readAnimeMediaState = { media },
        readUserRateState = { userRate },
        navigator = IdleNavigator,
        onUserRateClick = {}
    )
}

private const val PosterKey = "poster"
private const val NameKey = "name"
private const val InfoKey = "info"
private const val DescriptionKey = "description"