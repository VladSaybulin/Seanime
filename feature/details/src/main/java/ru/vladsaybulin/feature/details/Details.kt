package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.navigation.NavigationEvent
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.animeDetails
import ru.vladsaybulin.core.navigation.mangaDetails
import ru.vladsaybulin.core.navigation.searchByGenre
import ru.vladsaybulin.core.navigation.searchByPublisher
import ru.vladsaybulin.core.navigation.searchByStudio
import ru.vladsaybulin.core.ui.ErrorMessageColumn
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.text.SeanimeExpandableText
import ru.vladsaybulin.core.ui.text.onSeanimeTextLinkClickAdapter
import ru.vladsaybulin.feature.details.content.AuthorsCarousel
import ru.vladsaybulin.feature.details.content.CharactersBottomSheet
import ru.vladsaybulin.feature.details.content.CharactersCarousel
import ru.vladsaybulin.feature.details.content.RelatedBottomSheet
import ru.vladsaybulin.feature.details.content.ScreenshotsBottomSheet
import ru.vladsaybulin.feature.details.content.ScreenshotsCarousel
import ru.vladsaybulin.feature.details.content.SimilarAnimeBottomSheet
import ru.vladsaybulin.feature.details.content.SimilarMangaBottomSheet
import ru.vladsaybulin.feature.details.content.UserRateFab
import ru.vladsaybulin.feature.details.content.UserRateStatusSelectionBottomSheet
import ru.vladsaybulin.feature.details.content.VideosCarousel
import ru.vladsaybulin.feature.details.content.animeInformation
import ru.vladsaybulin.feature.details.content.mangaInformation
import ru.vladsaybulin.feature.details.content.mangaSimilarCarousel
import ru.vladsaybulin.feature.details.content.name
import ru.vladsaybulin.feature.details.content.poster
import ru.vladsaybulin.feature.details.content.relatedItems
import ru.vladsaybulin.feature.details.content.score
import ru.vladsaybulin.feature.details.content.similarAnimeCarousel
import ru.vladsaybulin.feature.details.content.userRateStatusDiagram
import ru.vladsaybulin.feature.details.model.getUserRateWithEntry
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.common.EntryStatus.Anons
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus

@Composable
fun DetailsRoute(
    navigator: SeanimeNavigator,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val enabledAutocorrect by viewModel.enabledAutocorrectStatus.collectAsStateWithLifecycle()
    val isAuthorized = viewModel.isAuthorized()

    LaunchedEffect(key1 = viewModel) {
        viewModel.refresh()
    }

    DetailsScreen(
        uiState = uiState,
        enabledAutocorrect = enabledAutocorrect,
        isAuthorized = isAuthorized,
        onRetry = viewModel::onRetry,
        refresh = viewModel::refresh,
        onCreateUserRate = viewModel::createUserRate,
        navigator = navigator
    )
}

@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    enabledAutocorrect: Boolean,
    isAuthorized: Boolean,
    onRetry: () -> Unit,
    refresh: suspend () -> Unit,
    onCreateUserRate: (UserRateStatus) -> Unit,
    navigator: SeanimeNavigator,
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {

        when (uiState) {
            is DetailsUiState.Error -> ErrorMessageColumn(
                description = if (uiState.throwable.message != null) {
                    { Text(uiState.throwable.message!!) }
                } else null,
                action = {
                    TextButton(onClick = onRetry) {
                        Text(text = stringResource(id = R.string.core_ui_error_retry))
                    }
                }
            )

            DetailsUiState.Loading -> DetailsLoading()

            is DetailsUiState.Success -> DetailsContent(
                state = uiState,
                enabledAutocorrect = enabledAutocorrect,
                isAuthorized = isAuthorized,
                refresh = refresh,
                onCreateUserRate = onCreateUserRate,
                navigator = navigator
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
    state: DetailsUiState.Success,
    enabledAutocorrect: Boolean,
    isAuthorized: Boolean,
    refresh: suspend () -> Unit,
    onCreateUserRate: (UserRateStatus) -> Unit,
    navigator: SeanimeNavigator,
) {
    var showAllCharacters by rememberSaveable { mutableStateOf(false) }
    var showAllRelatedEntries by rememberSaveable { mutableStateOf(false) }
    var showAllSimilarEntries by rememberSaveable { mutableStateOf(false) }
    var showAllScreenshots by rememberSaveable { mutableStateOf(false) }

    var showUserRateStatusSelection by remember { mutableStateOf(false) }

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()

    val visibleTopBar by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    val expandedFab by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        topBar = {
            DetailsTopBar(
                visibleTopBar = visibleTopBar,
                title = state.run { russianName ?: name },
                onBackClick = { navigator.back() },
                scrollBehavior = topAppBarScrollBehavior
            )
        },
        floatingActionButton = {
            UserRateFab(
                userRateStatus = state.userRate?.status ?: UserRateStatus.None,
                entryType = state.entryType,
                expanded = expandedFab,
                onClick = {
                    when {
                        !isAuthorized -> navigator.requireAuthDialog()

                        state.userRate != null -> navigator.userRate(state.getUserRateWithEntry())

                        enabledAutocorrect && state.status == Anons -> onCreateUserRate(
                            UserRateStatus.Planned
                        )

                        else -> showUserRateStatusSelection = true
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            state = listState,
            //FAB padding
            contentPadding = PaddingValues(bottom = 56.dp + 32.dp)
        ) {

            poster(
                poster = state.poster,
                topSpace = scaffoldPadding.calculateTopPadding(),
                onPosterClick = {
                    state.poster?.let { navigator.imageView(listOf(it), 0) }
                }
            )

            name(
                name = state.name,
                russianName = state.russianName,
            )

            if (state.entryType == EntryType.Manga) {
                mangaInformation(
                    state = state,
                    onSearchByGenre = { navigator.searchByGenre(state.searchType(), it) },
                    onSearchByPublisher = { navigator.searchByPublisher(state.searchType(), it) }
                )
            }

            if (state.entryType == EntryType.Anime) {
                animeInformation(
                    state = state,
                    onSearchByGenre = { navigator.searchByGenre(state.searchType(), it) },
                    onSearchByStudio = { navigator.searchByStudio(it) }
                )
            }

            item { Spacer(Modifier.height(24.dp)) }

            if (state.description != null) {
                item(key = "description") {
                    EntryDetailsDescription(
                        description = state.description,
                        onNavigationEvent = { }
                    )
                }
            }

            if (!state.authors.isNullOrEmpty()) {
                item(key = "authors") {
                    AuthorsCarousel(
                        authors = state.authors,
                        onAuthorClick = { navigator.personDetails(it) },
                        onShowAllClick = { navigator.authors(state.entryType, state.entryId) }
                    )
                }
            }

            if (state.score > 0f) {
                score(state.score, state.scoreStatisticsItems ?: emptyList())
            }

            if (!state.userRateStatusStatisticItems.isNullOrEmpty()) {
                userRateStatusDiagram(state.userRateStatusStatisticItems)
            }

            if (!state.related.isNullOrEmpty()) {
                relatedItems(
                    relatedEntries = state.related,
                    onAnimeClick = navigator::animeDetails,
                    onMangaClick = navigator::mangaDetails,
                    onShowAllClick = { showAllRelatedEntries = true }
                )
            }

            if (!state.characters.isNullOrEmpty()) {
                item(key = "characters") {
                    CharactersCarousel(
                        characters = state.characters,
                        onShowAllClick = {
                            showAllCharacters = true
                        },
                        onCharacterClick = navigator::characterDetails
                    )
                }
            }

            if (!state.screenshots.isNullOrEmpty()) {
                item(key = "screenshot") {
                    ScreenshotsCarousel(
                        screenshots = state.screenshots,
                        onScreenshotClick = {
                            navigator.imageView(state.screenshots, it)
                        },
                        onShowAllClick = { showAllScreenshots = true }
                    )
                }
            }

            if (!state.videos.isNullOrEmpty()) {
                item(key = "videos") {
                    VideosCarousel(
                        videos = state.videos,
                        onVideoClick = { },
                        onShowAllClick = { }
                    )
                }
            }

            if (!state.similarAnime.isNullOrEmpty()) {
                similarAnimeCarousel(
                    animes = state.similarAnime,
                    onShowAllClick = { showAllSimilarEntries = true },
                    onAnimeClick = navigator::animeDetails
                )
            }

            if (!state.similarManga.isNullOrEmpty()) {
                mangaSimilarCarousel(
                    mangas = state.similarManga,
                    onShowAllClick = { showAllSimilarEntries = true },
                    onMangaClick = navigator::mangaDetails
                )
            }
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(Unit) {
                refresh()
                pullToRefreshState.endRefresh()
            }
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
            PullToRefreshContainer(state = pullToRefreshState)
        }
    }

    if (state.characters != null && showAllCharacters) {
        CharactersBottomSheet(
            allCharacters = state.characters,
            onCharacterClick = navigator::characterDetails,
            onDismissRequest = { showAllCharacters = false }
        )
    }

    if (state.related != null && showAllRelatedEntries) {
        RelatedBottomSheet(
            related = state.related,
            onAnimeClick = navigator::animeDetails,
            onMangaClick = navigator::mangaDetails,
            onDismissRequest = { showAllRelatedEntries = false }
        )
    }

    if (state.screenshots != null && showAllScreenshots) {
        ScreenshotsBottomSheet(
            screenshots = state.screenshots,
            onScreenshotClick = { navigator.imageView(state.screenshots, it) },
            onDismissRequest = { showAllScreenshots = false }
        )
    }

    if (showAllSimilarEntries) {
        when {
            !state.similarAnime.isNullOrEmpty() -> SimilarAnimeBottomSheet(
                animes = state.similarAnime,
                onAnimeClick = navigator::animeDetails,
                onDismissRequest = { showAllSimilarEntries = false }
            )

            !state.similarManga.isNullOrEmpty() -> SimilarMangaBottomSheet(
                mangas = state.similarManga,
                onMangaClick = navigator::mangaDetails,
                onDismissRequest = { showAllSimilarEntries = false }
            )
        }
    }

    if (showUserRateStatusSelection) {
        UserRateStatusSelectionBottomSheet(
            enabledAutocorrect = enabledAutocorrect,
            entryType = state.entryType,
            entryStatus = state.status,
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

@Composable
private fun EntryDetailsDescription(
    description: SeanimeText,
    onNavigationEvent: (NavigationEvent) -> Unit,
) {
    SeanimeExpandableText(
        text = description,
        style = SeanimeTheme.typography.bodyMedium,
        modifier = Modifier.padding(HorizontalPadding),
        onLinkClick = onSeanimeTextLinkClickAdapter(
            onAnimeClick = { NavigationEvent.EntryDetails(EntryType.Anime, it) },
            onMangaClick = { NavigationEvent.EntryDetails(EntryType.Manga, it) },
            onCharacterClick = { NavigationEvent.CharacterDetails(it) },
            onPersonClick = { null },
            onUrlClick = { null },
            onAction = onNavigationEvent
        )
    )
}

private val HorizontalPadding = PaddingValues(horizontal = 16.dp)

