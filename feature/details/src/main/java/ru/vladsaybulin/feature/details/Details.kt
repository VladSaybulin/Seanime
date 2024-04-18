package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import ru.vladsaybulin.core.navigation.ImageViewArgs
import ru.vladsaybulin.core.navigation.SearchArgs
import ru.vladsaybulin.feature.details.content.AuthorsBottomSheet
import ru.vladsaybulin.feature.details.content.AuthorsCarousel
import ru.vladsaybulin.feature.details.content.CharactersBottomSheet
import ru.vladsaybulin.feature.details.content.CharactersCarousel
import ru.vladsaybulin.feature.details.content.Description
import ru.vladsaybulin.feature.details.content.RelatedBottomSheet
import ru.vladsaybulin.feature.details.content.ScreenshotsBottomSheet
import ru.vladsaybulin.feature.details.content.ScreenshotsCarousel
import ru.vladsaybulin.feature.details.content.SimilarCarousel
import ru.vladsaybulin.feature.details.content.UserRateFab
import ru.vladsaybulin.feature.details.content.UserRateStatusSelectionBottomSheet
import ru.vladsaybulin.feature.details.content.VideosCarousel
import ru.vladsaybulin.feature.details.content.animeInformation
import ru.vladsaybulin.feature.details.content.mangaInformation
import ru.vladsaybulin.feature.details.content.name
import ru.vladsaybulin.feature.details.content.poster
import ru.vladsaybulin.feature.details.content.relatedItems
import ru.vladsaybulin.feature.details.model.getUserRateWithEntry
import ru.vladsaybulin.model.EntryStatus.Anons
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.UserRateWithEntry

@Composable
fun DetailsRoute(
    modifier: Modifier = Modifier,
    onEntryClick: (EntryType, Long) -> Unit,
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
    onEntryClick: (EntryType, Long) -> Unit,
    onSearchClick: (SearchArgs) -> Unit,
    onAuthorClick: (Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onShowRequireAuthDialog: () -> Unit,
    onShowUserRate: (UserRateWithEntry) -> Unit,
    onShowImage: (ImageViewArgs) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is DetailsUiState.Error -> DetailsError(
            errorState = uiState,
            modifier = modifier,
            onRetry = onRetry
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

@Composable
private fun DetailsError(
    errorState: DetailsUiState.Error,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_message_title),
            style = ShikimoriTheme.typography.titleLarge
        )
        Text(
            text = stringResource(id = R.string.error_message),
            style = ShikimoriTheme.typography.bodyMedium
        )
        errorState.throwable.message?.let {
            Text(
                text = it,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                style = ShikimoriTheme.typography.bodySmall
            )
        }
        Button(onClick = onRetry) {
            Text(text = "Повторить")
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
    onEntryClick: (EntryType, Long) -> Unit,
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

    var expandedDescription by rememberSaveable { mutableStateOf(false) }

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
            .navigationBarsPadding()
            .padding(bottom = 80.dp)
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
            //FAB 56.dp + horizontal padding 16.dp
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

            if (state.descriptionHtml != null) {
                item(key = "description") {
                    Description(
                        descriptionHtml = state.descriptionHtml,
                        expanded = expandedDescription,
                        onExpandedChange = { expandedDescription = it },
                        modifier = HorizontalPaddingModifier
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

            if (!state.related.isNullOrEmpty()) {
                relatedItems(
                    relatedEntries = state.related,
                    onEntryClick = onEntryClick,
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

            if (state.similar.isNotEmpty()) {
                item(key = "similar") {
                    SimilarCarousel(
                        similarEntries = state.similar,
                        onEntryClick = onEntryClick,
                        onShowAll = { showAllSimilarEntries = true }
                    )
                }
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
            onEntryClick = onEntryClick,
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

private val HorizontalPadding = PaddingValues(horizontal = 16.dp)
private val HorizontalPaddingModifier = Modifier.padding(HorizontalPadding)

