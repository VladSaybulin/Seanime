package ru.vladsaybulin.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.designsystem.components.SeanimeHeader
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.FullScreenErrorMessage
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.anime.AnimeCarousel
import ru.vladsaybulin.core.ui.newstopic.newsTopicsFeed
import ru.vladsaybulin.core.ui.userrate.UserRateEntryCard
import ru.vladsaybulin.feature.home.navigation.HomeNavEvents
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.userrate.EditableUserRate
import ru.vladsaybulin.model.userrate.UserRateWithEntry

@Composable
fun HomeScreen(
    navEvents: HomeNavEvents,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        navEvents = navEvents
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    navEvents: HomeNavEvents
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(LocalScreenContentPadding.current)
            .fillMaxSize()
    ) {
        val topBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        Scaffold(
            topBar = {
                HomeTopBar(
                    me = (uiState as? HomeUiState.Success)?.me,
                    scrollBehavior = topBarScrollBehaviour,
                    onMeClick = navEvents.navigateToMe
                )
            },
            modifier = Modifier.nestedScroll(topBarScrollBehaviour.nestedScrollConnection)
        ) { scaffoldPadding ->
            Box(modifier = Modifier.padding(scaffoldPadding)) {
                HomeContent(
                    uiState = uiState,
                    navEvents = navEvents
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    navEvents: HomeNavEvents
) {
    when (uiState) {
        is HomeUiState.Success -> HomeBody(
            uiState = uiState,
            navEvents = navEvents
        )

        is HomeUiState.Error -> FullScreenErrorMessage(throwable = uiState.throwable)

        else -> LoadingHomeBody()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    me: BriefUser?,
    scrollBehavior: TopAppBarScrollBehavior,
    onMeClick: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.feature_home_title)) },
        actions = {
            IconButton(onClick = onMeClick) {
                val userImagePainter = me?.image?.x64Url?.let { rememberAsyncImagePainter(it) }
                    ?: rememberVectorPainter(SeanimeIcons.AccountCircle)
                Image(
                    painter = userImagePainter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(24.dp).clip(CircleShape),
                    colorFilter = me?.let { null } ?: ColorFilter.tint(SeanimeTheme.colorScheme.onSurface)
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun LoadingHomeBody() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun HomeBody(
    uiState: HomeUiState.Success,
    navEvents: HomeNavEvents,
) {
    val uriHandler = LocalUriHandler.current

    LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
        inProgressUserRatesPager(
            userRates = uiState.inProgressUserRates,
            onAnimeClick = { navEvents.navigateToTitleDetails(EntryType.Anime, it.id) },
            onMangaClick = { navEvents.navigateToTitleDetails(EntryType.Manga, it.id) },
            onEditClick = navEvents.showUserRateEditor
        )


        animeOngoingCarousel(
            ongoingAnime = uiState.ongoings,
            onAnimeClick = { navEvents.navigateToTitleDetails(EntryType.Anime, it.id) },
            onMoreClick = navEvents.navigateToSearchAnimeOngoing
        )

        newsTopicsHeader()
        newsTopicsFeed(
            newsTopics = uiState.newsTopics,
            onTopicClick = { uriHandler.openUri("$SHIKIMORI_NEWS_URL/${it.id}") },
            onUserClick = { uriHandler.openUri("$SHIKIMORI_USER_URL/${it.nickname}") },
            key = { "$NewsTopicKeyPrefix${it.id}" }
        )
        allNewsTopicsButton(onAllNewsTopicsClick = { uriHandler.openUri(SHIKIMORI_NEWS_URL) })
    }
}

fun LazyListScope.newsTopicsHeader() {
    item(key = NewsHeaderKey) {
        SeanimeHeader(modifier = Modifier.animateItem()) {
            Text(text = stringResource(id = R.string.feature_home_news_topics))
        }
    }

}

private fun LazyListScope.allNewsTopicsButton(
    onAllNewsTopicsClick: () -> Unit
) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .animateItem(),
            contentAlignment = Alignment.TopCenter
        ) {
            ElevatedButton(onClick = onAllNewsTopicsClick) {
                Text(text = stringResource(id = R.string.feature_home_all_news_topics))
            }
        }
    }
}

private fun LazyListScope.animeOngoingCarousel(
    ongoingAnime: ImmutableList<Anime>,
    onAnimeClick: (Anime) -> Unit,
    onMoreClick: () -> Unit
) {
    item(key = OngoingAnimesKey) {
        Column(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .animateItem()
        ) {
            SeanimeHeader(
                modifier = Modifier.clickable(onClick = onMoreClick),
                trailing = {
                    Icon(
                        imageVector = SeanimeIcons.ArrowForwardIos,
                        contentDescription = stringResource(id = R.string.feature_home_anime_ongoing_header_more),
                        modifier = Modifier.size(16.dp)
                    )
                }
            ) {
                Text(text = stringResource(id = R.string.feature_home_on_air_now))
            }
            AnimeCarousel(
                anime = ongoingAnime,
                onClick = onAnimeClick
            )
        }
    }
}

private fun LazyListScope.inProgressUserRatesPager(
    userRates: ImmutableList<UserRateWithEntry>,
    onAnimeClick: (Anime) -> Unit,
    onMangaClick: (Manga) -> Unit,
    onEditClick: (EditableUserRate) -> Unit,
    modifier: Modifier = Modifier,
) {
    item(key = InProgressUserRatesKey) {
        Box(modifier = Modifier.animateItem()) {
            val pagerState = rememberPagerState { userRates.size }

            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 8.dp,
                key = { userRates[it].userRate.id },
                modifier = modifier.let { if (userRates.isNotEmpty()) it.padding(bottom = 24.dp) else it }
            ) {
                UserRateEntryCard(
                    userRateWithEntry = userRates[it],
                    onAnimeClick = onAnimeClick,
                    onMangaClick = onMangaClick,
                    showUserRateBadge = true,
                    onEditClick = onEditClick
                )
            }
        }
    }
}

private const val InProgressUserRatesKey = "user_rates"
private const val OngoingAnimesKey = "ongoing"
private const val NewsHeaderKey = "news_header"
private const val NewsTopicKeyPrefix = "news"

private const val SHIKIMORI_NEWS_URL = "https://shikimori.one/forum/news"