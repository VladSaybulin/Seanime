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
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.core.navigation.NavigationEvent
import ru.vladsaybulin.core.navigation.args.EntryDetailsArgs
import ru.vladsaybulin.core.navigation.args.ImageViewArgs
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.core.ui.ErrorMessageColumn
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.core.ui.annotatedtext.SeanimeExpandableText
import ru.vladsaybulin.core.ui.annotatedtext.onSeanimeTextLinkClickAdapter
import ru.vladsaybulin.feature.details.content.AuthorsBottomSheet
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
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun DetailsRoute(
    modifier: Modifier = Modifier,
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onBackClick: () -> Unit,
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
        onEntryClick = onEntryClick,
        onSearchClick = onSearchClick,
        onAuthorClick = onAuthorClick,
        onCharacterClick = onCharacterClick,
        onShowRequireAuthDialog = onShowRequireAuthDialog,
        onShowUserRate = onShowUserRate,
        onShowImage = onShowImage,
        onBackClick = onBackClick,
        modifier = modifier,
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
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
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
                onEntryClick = onEntryClick,
                onSearchClick = onSearchClick,
                onAuthorClick = onAuthorClick,
                onCharacterClick = onCharacterClick,
                onShowRequireAuthDialog = onShowRequireAuthDialog,
                onShowUserRate = onShowUserRate,
                onShowImage = onShowImage,
                onBackClick = onBackClick,
                modifier = modifier,
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
    onEntryClick: (EntryDetailsArgs) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier
) {
    var showAllCharacters by rememberSaveable { mutableStateOf(false) }
    var showAllRelatedEntries by rememberSaveable { mutableStateOf(false) }
    var showAllSimilarEntries by rememberSaveable { mutableStateOf(false) }
    var showAllAuthors by rememberSaveable { mutableStateOf(false) }
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
        modifier = modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        topBar = {
            DetailsTopBar(
                visibleTopBar = visibleTopBar,
                title = state.run { russianName ?: name },
                onBackClick = onBackClick,
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
                        !isAuthorized -> onShowRequireAuthDialog()
                        state.userRate != null -> onShowUserRate(state.getUserRateWithEntry())
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
                onPosterClick = { onShowImage(state.posterViewParams()) }
            )

            name(
                name = state.name,
                russianName = state.russianName,
            )

            if (state.entryType == EntryType.Manga) {
                mangaInformation(
                    state = state,
                    onSearchClick = onSearchClick
                )
            }

            if (state.entryType == EntryType.Anime) {
                animeInformation(
                    state = state,
                    onSearchClick = onSearchClick
                )
            }

            item { Spacer(Modifier.height(24.dp)) }

            if (state.description != null) {
                item(key = "description") {
                    EntryDetailsDescription(
                        description = state.description,
                        onNavigationEvent = {  }
                    )
                }
            }

            if (!state.authors.isNullOrEmpty()) {
                item(key = "authors") {
                    AuthorsCarousel(
                        authors = state.authors,
                        onAuthorClick = onAuthorClick,
                        onShowAllClick = { showAllAuthors = true }
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
                    onEntryClick = { type, id -> onEntryClick(EntryDetailsArgs(type, id)) },
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
                        onCharacterClick = onCharacterClick
                    )
                }
            }

            if (!state.screenshots.isNullOrEmpty()) {
                item(key = "screenshot") {
                    ScreenshotsCarousel(
                        screenshots = state.screenshots,
                        onScreenshotClick = { onShowImage(state.screenshotViewParams(it)) },
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
                    onAnimeClick = { onEntryClick(EntryDetailsArgs(EntryType.Anime, it.id)) }
                )
            }

            if (!state.similarManga.isNullOrEmpty()) {
                mangaSimilarCarousel(
                    mangas = state.similarManga,
                    onShowAllClick = { showAllSimilarEntries = true },
                    onMangaClick = { onEntryClick(EntryDetailsArgs(EntryType.Manga, it.id)) }
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

    if (state.authors != null && showAllAuthors) {
        AuthorsBottomSheet(
            authors = state.authors,
            onAuthorClick = onAuthorClick,
            onDismissRequest = { showAllAuthors = false }
        )
    }

    if (state.characters != null && showAllCharacters) {
        CharactersBottomSheet(
            allCharacters = state.characters,
            onCharacterClick = onCharacterClick,
            onDismissRequest = { showAllCharacters = false }
        )
    }

    if (state.related != null && showAllRelatedEntries) {
        RelatedBottomSheet(
            related = state.related,
            onEntryClick = { type, id -> onEntryClick(EntryDetailsArgs(type, id)) },
            onDismissRequest = { showAllRelatedEntries = false }
        )
    }

    if (state.screenshots != null && showAllScreenshots) {
        ScreenshotsBottomSheet(
            screenshots = state.screenshots,
            onScreenshotClick = { onShowImage(state.screenshotViewParams(it)) },
            onDismissRequest = { showAllScreenshots = false }
        )
    }

    if (showAllSimilarEntries) {
        when {
            !state.similarAnime.isNullOrEmpty() -> SimilarAnimeBottomSheet(
                animes = state.similarAnime,
                onAnimeClick = { onEntryClick(EntryDetailsArgs(EntryType.Anime, it.id)) },
                onDismissRequest = { showAllSimilarEntries = false }
            )

            !state.similarManga.isNullOrEmpty() -> SimilarMangaBottomSheet(
                mangas = state.similarManga,
                onMangaClick = { onEntryClick(EntryDetailsArgs(EntryType.Manga, it.id)) },
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
        style = ShikimoriTheme.typography.bodyMedium,
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
private val HorizontalPaddingModifier = Modifier.padding(HorizontalPadding)

