package ru.vladsaybulin.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.navigation.SeanimeNavigator
import ru.vladsaybulin.core.navigation.animeDetails
import ru.vladsaybulin.core.navigation.args.SearchArgs
import ru.vladsaybulin.core.navigation.mangaDetails
import ru.vladsaybulin.core.ui.FullScreenErrorMessage
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.anime.AnimeCarousel
import ru.vladsaybulin.core.ui.newstopic.newsTopicsFeed
import ru.vladsaybulin.core.ui.userrate.UserRateEntryCard
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.search.SearchType
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun HomeRoute(
    navigator: SeanimeNavigator,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        navigator = navigator
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    navigator: SeanimeNavigator,
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {
        when (uiState) {
            is HomeUiState.Success -> HomeContent(
                uiState = uiState,
                navigator = navigator
            )

            is HomeUiState.Error -> FullScreenErrorMessage(throwable = uiState.throwable)

            else -> HomeLoading()
        }
    }
}

@Composable
fun HomeLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    uiState: HomeUiState.Success,
    navigator: SeanimeNavigator,
) {
    val topBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Shikimori") },
                scrollBehavior = topBarScrollBehaviour
            )
        },
        modifier = Modifier.nestedScroll(topBarScrollBehaviour.nestedScrollConnection)
    ) { scaffoldPadding ->
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier.padding(scaffoldPadding)
        ) {

            if (uiState.inProgressUserRates.isNotEmpty()) {
                inProgressUserRatesPager(
                    userRates = uiState.inProgressUserRates,
                    onAnimeClick = navigator::animeDetails,
                    onMangaClick = navigator::mangaDetails
                )
                sectionSpace()
            }

            animeOngoingHeader(onMoreClick = navigator::searchOngoingAnimes)
            animeOngoingCarousel(
                ongoingAnime = uiState.ongoings,
                onAnimeClick = navigator::animeDetails
            )

            sectionSpace()

            newsTopicsHeader()
            newsTopicsFeed(
                newsTopics = uiState.newsTopics,
                onTopicClick = {
                    //TODO
                    navigator.externalLink("https://shikimori.one/forum/news/${it.id}")
                },
                onUserClick = {
                    //TODO
                    navigator.externalLink(it.url)
                }
            )
            allNewsTopicsButton(onAllNewsTopicsClick = navigator::news)

            bottomSpace()
        }
    }
}

private fun LazyListScope.allNewsTopicsButton(
    onAllNewsTopicsClick: () -> Unit
) {
    item {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            ElevatedButton(onClick = onAllNewsTopicsClick) {
                Text(text = stringResource(id = R.string.feature_home_all_news_topics))
            }
        }
    }
}

private fun LazyListScope.bottomSpace() {
    item { Spacer(modifier = Modifier.height(80.dp)) }
}

private fun LazyListScope.animeOngoingHeader(
    onMoreClick: () -> Unit
) {
    item {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.feature_home_on_air_now),
                style = SeanimeTheme.typography.titleMedium
            )
            TextButton(onClick = onMoreClick) {
                Text(text = stringResource(id = R.string.feature_home_more))
            }
        }
    }
}

private fun LazyListScope.newsTopicsHeader() {
    item {
        Column {
            Text(
                text = stringResource(R.string.feature_home_news_topics),
                style = SeanimeTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

private fun LazyListScope.sectionSpace() {
    item {
        Spacer(modifier = Modifier.height(24.dp))
    }
}

private fun LazyListScope.animeOngoingCarousel(
    ongoingAnime: ImmutableList<Anime>,
    onAnimeClick: (Anime) -> Unit
) {
    item {
        AnimeCarousel(
            anime = ongoingAnime,
            onClick = onAnimeClick
        )
    }
}

private fun LazyListScope.inProgressUserRatesPager(
    userRates: ImmutableList<UserRateWithEntry>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    modifier: Modifier = Modifier,
) {
    item @OptIn(ExperimentalFoundationApi::class) {
        val pagerState = rememberPagerState { userRates.size }

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 16.dp),
            pageSpacing = 8.dp,
            key = { userRates[it].userRate.id },
            modifier = modifier
        ) {
            UserRateEntryCard(
                userRateWithEntry = userRates[it],
                onAnimeClick = onAnimeClick,
                onMangaClick = onMangaClick,
                showUserRateBadge = true
            )
        }
    }
}

private val AnimeOngoingSearchArgs = SearchArgs(
    searchType = SearchType.Anime,
    entryStatus = EntryStatus.Ongoing
)