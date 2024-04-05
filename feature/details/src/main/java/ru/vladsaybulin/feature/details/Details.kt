package ru.vladsaybulin.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.vladsaybulin.core.designsystem.theme.ShikimoriTheme
import ru.vladsaybulin.feature.details.content.AnimeInformation
import ru.vladsaybulin.feature.details.content.AuthorsBottomSheet
import ru.vladsaybulin.feature.details.content.AuthorsCarousel
import ru.vladsaybulin.feature.details.content.CharactersBottomSheet
import ru.vladsaybulin.feature.details.content.CharactersCarousel
import ru.vladsaybulin.feature.details.content.Description
import ru.vladsaybulin.feature.details.content.EntryDetailsName
import ru.vladsaybulin.feature.details.content.EntryDetailsPoster
import ru.vladsaybulin.feature.details.content.MangaInformation
import ru.vladsaybulin.feature.details.content.RelatedBottomSheet
import ru.vladsaybulin.feature.details.content.ScreenshotsBottomSheet
import ru.vladsaybulin.feature.details.content.ScreenshotsCarousel
import ru.vladsaybulin.feature.details.content.SimilarCarousel
import ru.vladsaybulin.feature.details.content.UserRateFab
import ru.vladsaybulin.feature.details.content.UserRateStatusSelectionBottomSheet
import ru.vladsaybulin.feature.details.content.VideosCarousel
import ru.vladsaybulin.feature.details.content.relatedItems
import ru.vladsaybulin.feature.userrate.UserRateEditorContext
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Screenshot
import ru.vladsaybulin.model.UserRate
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.Video

@Composable
fun DetailsRoute(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = hiltViewModel(),
    onEntryClick: (EntryType, Long) -> Unit,
    onScreenshotClick: (List<Screenshot>, screenshotIndex: Int) -> Unit,
    onEditUserRateClick: (UserRate, UserRateEditorContext) -> Unit,
    onGenreClick: (Long) -> Unit = {},
    onPublisherClick: (Long) -> Unit = {},
    onStudioClick: (Long) -> Unit = {},
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userRate by viewModel.userRate.collectAsStateWithLifecycle()
    val enabledAutocorrect by viewModel.enabledAutocorrectStatus.collectAsStateWithLifecycle()

    DetailsScreen(
        uiState = uiState,
        userRate = userRate,
        enabledAutocorrect = enabledAutocorrect,
        onRetry = viewModel::onRetry,
        onRefresh = viewModel::onRefresh,
        onEntryClick = onEntryClick,
        onScreenshotClick = onScreenshotClick,
        onGenreClick = onGenreClick,
        onPublisherClick = onPublisherClick,
        onStudioClick = onStudioClick,
        onBackClick = onBackClick,
        modifier = modifier,
        onEditUserRateClick = {
            val context = viewModel.getUserRateEditorContext() ?: return@DetailsScreen
            onEditUserRateClick(checkNotNull(userRate), context)
        },
        onCreateUserRate = viewModel::createUserRate
    )
}

@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    userRate: UserRate?,
    enabledAutocorrect: Boolean,
    onRetry: () -> Unit,
    onRefresh: suspend () -> Unit,
    onEntryClick: (EntryType, Long) -> Unit,
    onScreenshotClick: (List<Screenshot>, screenshotIndex: Int) -> Unit,
    onEditUserRateClick: () -> Unit,
    onCreateUserRate: (UserRateStatus) -> Unit,
    onGenreClick: (Long) -> Unit,
    onPublisherClick: (Long) -> Unit,
    onStudioClick: (Long) -> Unit,
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
            userRate = userRate,
            enabledAutocorrect = enabledAutocorrect,
            onEntryClick = onEntryClick,
            onBackClick = onBackClick,
            onRefresh = onRefresh,
            modifier = modifier,
            onEditUserRateClick = onEditUserRateClick,
            onCreateUserRate = onCreateUserRate,
            onGenreClick = onGenreClick,
            onPublisherClick = onPublisherClick,
            onStudioClick = onStudioClick,
            onAuthorClick = {},
            onCharacterClick = {},
            onScreenshotClick = { index ->
                onScreenshotClick(checkNotNull(uiState.screenshots), index)
            },
            onVideoClick = {}
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
    userRate: UserRate?,
    enabledAutocorrect: Boolean,
    onRefresh: suspend () -> Unit,
    onEditUserRateClick: () -> Unit,
    onAuthorClick: (Long) -> Unit,
    onEntryClick: (EntryType, Long) -> Unit,
    onCharacterClick: (Long) -> Unit,
    onScreenshotClick: (screenshotIndex: Int) -> Unit,
    onVideoClick: (Video) -> Unit,
    onCreateUserRate: (UserRateStatus) -> Unit,
    onGenreClick: (Long) -> Unit,
    onPublisherClick: (Long) -> Unit,
    onStudioClick: (Long) -> Unit,
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
                userRateStatus = userRate?.status ?: UserRateStatus.None,
                entryType = state.entryType,
                expanded = expandedFab,
                onClick = {
                    if (userRate != null) {
                        onEditUserRateClick()
                    } else {
                        if (state.status == EntryStatus.Anons) {
                            onCreateUserRate(UserRateStatus.Planned)
                        } else {
                            showUserRateStatusSelection = true
                        }
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        LazyColumn(
            state = listState,
        ) {

            item(key = "poster") {
                EntryDetailsPoster(
                    poster = state.poster,
                    topSpacingDp = scaffoldPadding.calculateTopPadding()
                )
            }

            item(key = "poster_name_space") {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item(key = "name") {
                EntryDetailsName(
                    name = state.name,
                    russianName = state.russianName,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (state.entryType == EntryType.Manga) {
                item(key = "manga_info") {
                    MangaInformation(
                        state = state,
                        onPublisherClick = onPublisherClick,
                        onGenreClick = onGenreClick
                    )
                }
            }

            if (state.entryType == EntryType.Anime) {
                item(key = "anime_info") {
                    AnimeInformation(
                        state = state,
                        onStudioClick = onStudioClick,
                        onGenreClick = onGenreClick
                    )
                }
            }

            item {
                Spacer(Modifier.height(24.dp))
            }

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
                        onScreenshotClick = { onScreenshotClick(it) },
                        onShowAllClick = { showAllScreenshots = true }
                    )
                }
            }

            if (!state.videos.isNullOrEmpty()) {
                item(key = "videos") {
                    VideosCarousel(
                        videos = state.videos,
                        onVideoClick = onVideoClick,
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

            item(key = "bottom_padding") {
                //56dp (FAB height) + 16 dp (FAB padding) * 2 = 88 dp
                val navigationBarsBottomPadding = with(LocalDensity.current) {
                    WindowInsets.navigationBars.getBottom(this).toDp()
                }
                Spacer(modifier = Modifier.height(88.dp + navigationBarsBottomPadding))
            }
        }

        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(Unit) {
                onRefresh()
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
            onScreenshotClick = onScreenshotClick,
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

