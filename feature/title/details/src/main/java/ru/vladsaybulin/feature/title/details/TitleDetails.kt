package ru.vladsaybulin.feature.title.details

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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ru.vladsaybulin.core.designsystem.components.SeanimeHeader
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.strings.LocalTitleStrings
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.feature.title.details.content.DetailsTopBar
import ru.vladsaybulin.feature.title.details.content.PreviewScoreStatistics
import ru.vladsaybulin.feature.title.details.content.PreviewUserRateStatusStatistics
import ru.vladsaybulin.feature.title.details.content.RequireAuthDialog
import ru.vladsaybulin.feature.title.details.content.TitleAuthors
import ru.vladsaybulin.feature.title.details.content.TitleCharacters
import ru.vladsaybulin.feature.title.details.content.TitleDescription
import ru.vladsaybulin.feature.title.details.content.TitleInfo
import ru.vladsaybulin.feature.title.details.content.TitleName
import ru.vladsaybulin.feature.title.details.content.TitlePoster
import ru.vladsaybulin.feature.title.details.content.TitleScore
import ru.vladsaybulin.feature.title.details.content.TitleScreenshots
import ru.vladsaybulin.feature.title.details.content.TitleSimilarAnimes
import ru.vladsaybulin.feature.title.details.content.TitleSimilarMangas
import ru.vladsaybulin.feature.title.details.content.TitleUserRateStatusDiagram
import ru.vladsaybulin.feature.title.details.content.TitleVideos
import ru.vladsaybulin.feature.title.details.content.UserRateFab
import ru.vladsaybulin.feature.title.details.content.UserRateStatusSelectionBottomSheet
import ru.vladsaybulin.feature.title.details.navigation.IdleTitleDetailsNavEvents
import ru.vladsaybulin.feature.title.details.navigation.TitleDetailsNavEvents
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.common.DataSlice
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryStatus.Anons
import ru.vladsaybulin.model.common.EntryStatus.Ongoing
import ru.vladsaybulin.model.common.EntryStatus.Released
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.common.StatisticsItem
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.person.Person
import ru.vladsaybulin.model.person.PersonWithRoles
import ru.vladsaybulin.model.related.RelatedAnime
import ru.vladsaybulin.model.related.RelatedManga
import ru.vladsaybulin.model.related.RelatedTitle
import ru.vladsaybulin.model.related.RelationType
import ru.vladsaybulin.model.search.SeasonOfYear
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.core.ui2.entry.related.RelatedTitleItem
import java.security.KeyStore.Entry

@Composable
fun TitleDetailsScreen(
    navEvents: TitleDetailsNavEvents,
    viewModel: TitleDetailsViewModel = hiltViewModel(),
) {
    val detailsState by viewModel.detailsState.collectAsStateWithLifecycle()
    val rolesState by viewModel.rolesState.collectAsStateWithLifecycle()
    val similarState by viewModel.similarState.collectAsStateWithLifecycle()
    val userRateState by viewModel.userRateState.collectAsStateWithLifecycle()
    val enabledAutocorrect by viewModel.enabledAutocorrectStatus.collectAsStateWithLifecycle()

    DetailsScreen(
        detailsState = detailsState,
        rolesState = rolesState,
        similarState = similarState,
        userRateState = userRateState,
        enabledAutocorrect = enabledAutocorrect,
        onRetry = viewModel::onRetry,
        refresh = viewModel::refresh,
        onCreateUserRate = viewModel::createUserRate,
        navEvents = navEvents
    )
}

@Composable
fun DetailsScreen(
    detailsState: TitleDetailsState,
    rolesState: RolesState,
    similarState: SimilarState,
    userRateState: UserRateState,
    enabledAutocorrect: Boolean,
    onRetry: () -> Unit,
    refresh: suspend () -> Unit,
    onCreateUserRate: (UserRateStatus) -> Unit,
    navEvents: TitleDetailsNavEvents,
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {
        when (detailsState) {
            TitleDetailsState.Loading -> DetailsLoading()

            is TitleDetailsState.Success -> DetailsContent(
                detailsState,
                rolesState,
                similarState,
                userRateState,
                enabledAutocorrect = enabledAutocorrect,
                refresh = refresh,
                onCreateUserRate = onCreateUserRate,
                navEvents = navEvents
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
    detailsState: TitleDetailsState.Success,
    rolesState: RolesState,
    similarState: SimilarState,
    userRateState: UserRateState,
    enabledAutocorrect: Boolean,
    refresh: suspend () -> Unit,
    onCreateUserRate: (UserRateStatus) -> Unit,
    navEvents: TitleDetailsNavEvents
) {
    var showUserRateStatusSelection by remember { mutableStateOf(false) }
    val (showRequireAuthDialog, setShowRequireAuthDialog) = remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()

    val visibleTopBar by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val expandedFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    CompositionLocalProvider(value = LocalTitleStrings provides detailsState.entryType) {
        ProvideTitleStringsByType(detailsState.entryType) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    coroutineScope.launch {
                        isRefreshing = true
                        refresh()
                        isRefreshing = false
                    }
                }
            ) {
                Scaffold(
                    modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
                    topBar = {
                        DetailsTopBar(
                            visibleTopBar = visibleTopBar,
                            title = detailsState.run { russianName ?: name },
                            onBackClick = navEvents.navigateUp,
                            scrollBehavior = topAppBarScrollBehavior
                        )
                    },
                    floatingActionButton = {
                        val userRate = (userRateState as? UserRateState.Success)?.userRate
                        UserRateFab(
                            userRateState = userRateState,
                            expanded = expandedFab,
                            onClick = {
                                when (userRateState) {
                                    is UserRateState.NotAuthorized -> setShowRequireAuthDialog(true)
                                    is UserRateState.Success -> navEvents.showUserRateEditor(
                                        createEditableUserRate(detailsState, userRateState)
                                    )

                                    is UserRateState.NoUserRate -> if (detailsState.status == Anons) {
                                        onCreateUserRate(UserRateStatus.Planned)
                                    } else showUserRateStatusSelection = true

                                    else -> {}
                                }
                            }
                        )
                    }
                ) { scaffoldPadding ->
                    val uriHandler = LocalUriHandler.current

                    LazyColumn(
                        state = listState,
                        //FAB padding
                        contentPadding = PaddingValues(bottom = 56.dp + 32.dp)
                    ) {

                        titlePoster(
                            posterUrl = detailsState.poster?.originalUrl,
                            topSpace = scaffoldPadding.calculateTopPadding(),
                            onClick = {
                                detailsState.poster?.let {
                                    navEvents.showFullScreenImage(
                                        listOf(it),
                                        0
                                    )
                                }
                            }
                        )

                        gutterSpacer()
                        titleName(
                            name = detailsState.name,
                            russianName = detailsState.russianName
                        )

                        gutterSpacer()
                        titleInfo(
                            animeKind = detailsState.animeKind,
                            mangaKind = detailsState.mangaKind,
                            status = detailsState.status,
                            episodes = detailsState.episodes,
                            episodesAired = detailsState.episodesAired,
                            episodeDuration = detailsState.episodeDuration,
                            chapters = detailsState.chapters,
                            volumes = detailsState.volumes,
                            nextEpisodeAt = detailsState.nextEpisodeAt,
                            airedOn = detailsState.airedOn,
                            releasedOn = detailsState.releasedOn,
                            timePeriodAiring = detailsState.season,
                            rating = detailsState.rating,
                            studios = detailsState.studios,
                            publishers = detailsState.publishers,
                            genres = detailsState.genres,
                            onStudioClick = {
                                navEvents.navigateToSearchByStudio(
                                    detailsState.searchType(),
                                    it.id
                                )
                            },
                            onPublisherClick = {
                                navEvents.navigateToSearchByPublisher(
                                    detailsState.searchType(),
                                    it.id
                                )
                            },
                            onGenreClick = {
                                navEvents.navigateToSearchByGenre(
                                    detailsState.searchType(),
                                    it.kind,
                                    it.id
                                )
                            }
                        )

                        detailsState.description?.takeIf { it.text.isNotEmpty() }
                            ?.let { description ->
                                gutterSpacer()
                                titleDescription(
                                    description = description,
                                    onAnimeClick = {
                                        navEvents.navigateToTitleDetails(
                                            EntryType.Anime,
                                            it
                                        )
                                    },
                                    onMangaClick = {
                                        navEvents.navigateToTitleDetails(
                                            EntryType.Manga,
                                            it
                                        )
                                    },
                                    onCharacterClick = navEvents.navigateToCharacterDetails,
                                    onPersonClick = navEvents.navigateToPersonDetails,
                                    onUrlClick = uriHandler::openUri
                                )
                            }

                        (rolesState as? RolesState.Success)?.mainAuthors?.takeIf { it.isNotEmpty() }
                            ?.let { authors ->
                                gutterSpacer()
                                titleAuthors(
                                    authors = authors,
                                    onAuthorClick = { navEvents.navigateToPersonDetails(it.id) },
                                    onMoreClick = {
                                        navEvents.navigateToTitleAuthors(
                                            detailsState.entryType,
                                            detailsState.entryId
                                        )
                                    }
                                )
                            }

                        if (detailsState.score > 0f) {
                            gutterSpacer()
                            titleScore(
                                score = detailsState.score,
                                stats = detailsState.scoreStatisticsItems
                            )
                        }

                        if (!detailsState.userRateStatusStatisticItems.isNullOrEmpty()) {
                            gutterSpacer()
                            titleUserRateStatusDiagram(detailsState.userRateStatusStatisticItems)
                        }

                        detailsState.relatedSlice?.let { dataSlice ->
                            gutterSpacer()
                            titleRelated(
                                relatedEntriesSlice = dataSlice,
                                onTitleClick = navEvents.navigateToTitleDetails,
                                onMoreClick = {
                                    navEvents.navigateToTitleRelated(
                                        detailsState.entryType,
                                        detailsState.entryId
                                    )
                                }
                            )
                        }

                        (rolesState as? RolesState.Success)?.mainCharacters?.takeIf { it.isNotEmpty() }
                            ?.let { characters ->
                                gutterSpacer()
                                titleCharacters(
                                    characters = characters,
                                    onCharacterClick = { navEvents.navigateToCharacterDetails(it.id) },
                                    onMoreClick = {
                                        navEvents.navigateToTitleCharacters(
                                            detailsState.entryType,
                                            detailsState.entryId
                                        )
                                    }
                                )
                            }

                        detailsState.screenshotsSlice?.let { dataSlice ->
                            gutterSpacer()
                            titleScreenshots(
                                screenshotsSlice = dataSlice,
                                onScreenshotClick = { initialIndex ->
                                    navEvents.showFullScreenImage(
                                        detailsState.allScreenshots,
                                        initialIndex
                                    )
                                },
                                onMoreClick = {
                                    navEvents.navigateToTitleScreenshots(
                                        detailsState.entryType,
                                        detailsState.entryId
                                    )
                                }
                            )
                        }

                        detailsState.videosSlice?.let { videosSlice ->
                            gutterSpacer()
                            titleVideos(
                                videosSlice = videosSlice,
                                onVideoClick = { uriHandler.openUri(it.videoUrl) },
                                onMoreClick = {
                                    navEvents.navigateToTitleVideos(
                                        detailsState.entryType,
                                        detailsState.entryId
                                    )
                                }
                            )
                        }

                        when (similarState) {
                            SimilarState.Empty -> Unit
                            SimilarState.Loading -> Unit
                            is SimilarState.Animes -> {
                                gutterSpacer()
                                titleSimilarAnimes(
                                    similarAnimes = similarState.animes,
                                    onAnimeClick = {
                                        navEvents.navigateToTitleDetails(
                                            EntryType.Anime,
                                            it.id
                                        )
                                    }
                                )
                            }

                            is SimilarState.Mangas -> {
                                gutterSpacer()
                                titleSimilarMangas(
                                    similarMangas = similarState.mangas,
                                    onMangaClick = {
                                        navEvents.navigateToTitleDetails(
                                            EntryType.Manga,
                                            it.id
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showRequireAuthDialog) {
            RequireAuthDialog(
                authWithShikimori = {
                    navEvents.authWithShikimori()
                    setShowRequireAuthDialog(false)
                },
                onDismissRequest = {
                    setShowRequireAuthDialog(false)
                }
            )
        }

        if (showUserRateStatusSelection) {
            UserRateStatusSelectionBottomSheet(
                enabledAutocorrect = enabledAutocorrect,
                entryStatus = detailsState.status,
                onStatusClick = {
                    onCreateUserRate(it)
                    showUserRateStatusSelection = false
                },
                onDismissRequest = {
                    showUserRateStatusSelection = false
                }
            )
        }
    }
}

private fun LazyListScope.gutterSpacer() {
    item {
        Spacer(modifier = Modifier.height(24.dp))
    }
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
    animeKind: AnimeKind,
    mangaKind: MangaKind,
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
            animeKind = animeKind,
            mangaKind = mangaKind,
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
    relatedEntriesSlice: DataSlice<RelatedTitle>,
    onTitleClick: (EntryType, Long) -> Unit,
    onMoreClick: () -> Unit
) {
    dataSliceHeader(
        dataSlice = relatedEntriesSlice,
        headerTextId = R.string.related,
        onMoreClick = onMoreClick
    )

    items(items = relatedEntriesSlice.data) {
        RelatedTitleItem(
            relatedTitle = it,
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
    screenshotsSlice: DataSlice<Image>,
    onScreenshotClick: (index: Int) -> Unit,
    onMoreClick: () -> Unit
) {
    dataSliceHeader(
        dataSlice = screenshotsSlice,
        headerTextId = R.string.screenshots,
        onMoreClick = onMoreClick
    )

    item {
        TitleScreenshots(
            screenshots = screenshotsSlice.data,
            onScreenshotClick = onScreenshotClick
        )
    }
}


private fun LazyListScope.titleVideos(
    videosSlice: DataSlice<Video>,
    onVideoClick: (Video) -> Unit,
    onMoreClick: () -> Unit
) {
    dataSliceHeader(
        dataSlice = videosSlice,
        headerTextId = R.string.videos,
        onMoreClick = onMoreClick
    )

    item {
        TitleVideos(
            videos = videosSlice.data,
            onVideoClick = onVideoClick
        )
    }
}

private fun LazyListScope.titleSimilarAnimes(
    similarAnimes: List<Anime>,
    onAnimeClick: (Anime) -> Unit
) {
    header(headerTextId = R.string.similar)

    item {
        TitleSimilarAnimes(
            animes = similarAnimes,
            onAnimeClick = onAnimeClick
        )
    }
}

private fun LazyListScope.titleSimilarMangas(
    similarMangas: List<Manga>,
    onMangaClick: (Manga) -> Unit
) {
    header(headerTextId = R.string.similar)

    item {
        TitleSimilarMangas(
            mangas = similarMangas,
            onMangaClick = onMangaClick
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

private fun LazyListScope.dataSliceHeader(
    dataSlice: DataSlice<*>,
    headerTextId: Int,
    onMoreClick: () -> Unit
) {
    if (dataSlice.hasMore) {
        clickableHeader(headerTextId, onMoreClick)
    } else {
        header(headerTextId)
    }
}

@Composable
@Preview
fun EntryDetailsScreenPreview() {
    val detailsState = remember {
        TitleDetailsState.Success(
            entryType = EntryType.Anime,
            entryId = 21,
            poster = Image("", ""),
            name = "One Piece",
            russianName = "Ван-Пис",
            status = Ongoing,
            animeKind = AnimeKind.Tv,
            mangaKind = MangaKind.None,
            score = 8.82f,
            episodes = 0,
            episodesAired = 1114,
            episodeDuration = 23,
            chapters = 0,
            volumes = 0,
            nextEpisodeAt = Clock.System.now(),
            airedOn = IncompleteDate(day = 11, month = 8, year = 1999),
            season = TimePeriodAiring.Season(SeasonOfYear.Fall, 1999),
            releasedOn = null,
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
            scoreStatisticsItems = PreviewScoreStatistics,
            userRateStatusStatisticItems = PreviewUserRateStatusStatistics,
            relatedSlice = DataSlice(
                data = listOf(
                    RelatedAnime(
                        anime = Anime(
                            id = 813,
                            name = "Dragon Ball Z",
                            russianName = "Драконий жемчуг Зет",
                            poster = null,
                            kind = AnimeKind.Tv,
                            status = Released,
                            score = 8.18f,
                            episodes = 291,
                            episodesAired = 1,
                            airedOn = IncompleteDate(26, 4, 1989),
                            releasedOn = IncompleteDate(31, 1, 1996),
                            userRate = null
                        ),
                        relationType = RelationType.Character
                    ),
                    RelatedManga(
                        manga = Manga(
                            id = 13,
                            name = "One Piece",
                            russianName = "Ван пис",
                            poster = null,
                            kind = MangaKind.Manga,
                            status = Ongoing,
                            score = 9.22f,
                            chapters = 0,
                            volumes = 0,
                            airedOn = IncompleteDate(22, 7, 1997),
                            releasedOn = null
                        ),
                        relationType = RelationType.Adaptation
                    )
                ),
                hasMore = true
            ),
            allScreenshots = List(5) { Image("", "") },
            screenshotsSlice = DataSlice(
                data = List(5) { Image("", "") },
                hasMore = false
            ),
            videosSlice = DataSlice(emptyList(), false)
        )
    }

    val rolesState = remember {
        RolesState.Success(
            mainAuthors = listOf(
                PersonWithRoles(
                    person = Person(
                        id = 1,
                        originalName = "Echiro Oda",
                        russianName = "Эйтиро Ода",
                        poster = null
                    ),
                    roles = listOf("Original Creator"),
                    isMain = true
                )
            ),
            mainCharacters = listOf(
                Character(
                    id = 40,
                    originalName = "Luffy Monkey D.",
                    russianName = "Луффи Монки Д.",
                    poster = null
                ),
                Character(
                    id = 62,
                    originalName = "Zoro Roronoa",
                    russianName = "Зоро Ророноа",
                    poster = null
                ),
                Character(
                    id = 62,
                    originalName = "Nami",
                    russianName = "Нами",
                    poster = null
                ),
            )
        )
    }

    SeanimeTheme {
        DetailsContent(
            detailsState = detailsState,
            rolesState = rolesState,
            similarState = SimilarState.Empty,
            userRateState = UserRateState.NoUserRate,
            enabledAutocorrect = false,
            refresh = {},
            onCreateUserRate = {},
            navEvents = IdleTitleDetailsNavEvents
        )
    }
}

private const val PosterKey = "poster"
private const val NameKey = "name"
private const val InfoKey = "info"
private const val DescriptionKey = "description"