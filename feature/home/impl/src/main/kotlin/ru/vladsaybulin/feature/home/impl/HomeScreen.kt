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

package ru.vladsaybulin.feature.home.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import ru.vladsaybulin.core.designsystem.components.SeanimeHeader
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.ui.FullScreenErrorMessage
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.core.ui.ProfileButton
import ru.vladsaybulin.core.ui.newstopic.newsTopicsFeed
import ru.vladsaybulin.core.ui2.entry.EntryCarousel
import ru.vladsaybulin.core.ui2.entry.anime.animeCarouselItems
import ru.vladsaybulin.core.ui2.entry.userrate.UserRateItem
import ru.vladsaybulin.core.ui2.strings.compose.ProvideTitleStringsByType
import ru.vladsaybulin.feature.rate.editor.api.navigation.TitleReference
import ru.vladsaybulin.feature.rate.editor.api.navigation.titleReference
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.userrate.UserRateContext
import ru.vladsaybulin.model.userrate.UserRateValues
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.model.userrate.extractRateContext
import ru.vladsaybulin.model.userrate.toUserRateValues

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onAllNewsTopicsClick: () -> Unit,
    onAnimeClick: (Long) -> Unit,
    onTopicClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onMyProfileClick: () -> Unit,
    onUserClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
    onExploreAnimeOngoingClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onAllNewsTopicsClick = onAllNewsTopicsClick,
        onAnimeClick = onAnimeClick,
        onTopicClick = onTopicClick,
        onMangaClick = onMangaClick,
        onMyProfileClick = onMyProfileClick,
        onUserClick = onUserClick,
        onRateClick = onRateClick,
        onExploreAnimeOngoingClick = onExploreAnimeOngoingClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onAllNewsTopicsClick: () -> Unit,
    onAnimeClick: (Long) -> Unit,
    onTopicClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onMyProfileClick: () -> Unit,
    onUserClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
    onExploreAnimeOngoingClick: () -> Unit
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
                    onMeClick = onMyProfileClick
                )
            },
            modifier = Modifier.nestedScroll(topBarScrollBehaviour.nestedScrollConnection)
        ) { scaffoldPadding ->
            Box(modifier = Modifier.padding(scaffoldPadding)) {
                HomeContent(
                    uiState = uiState,
                    onAllNewsTopicsClick = onAllNewsTopicsClick,
                    onAnimeClick = onAnimeClick,
                    onExploreAnimeOngoingClick = onExploreAnimeOngoingClick,
                    onMangaClick = onMangaClick,
                    onRateClick = onRateClick,
                    onTopicClick = onTopicClick,
                    onUserClick = onUserClick,
                )
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAllNewsTopicsClick: () -> Unit,
    onAnimeClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onExploreAnimeOngoingClick: () -> Unit,
    onTopicClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
    onUserClick: (Long) -> Unit
) {
    when (uiState) {
        is HomeUiState.Success -> HomeBody(
            uiState = uiState,
            onAllNewsTopicsClick = onAllNewsTopicsClick,
            onAnimeClick = onAnimeClick,
            onExploreAnimeOngoingClick = onExploreAnimeOngoingClick,
            onMangaClick = onMangaClick,
            onRateClick = onRateClick,
            onTopicClick = onTopicClick,
            onUserClick = onUserClick
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
            ProfileButton(image = me?.image, onClick = onMeClick)
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
    onAllNewsTopicsClick: () -> Unit,
    onAnimeClick: (Long) -> Unit,
    onExploreAnimeOngoingClick: () -> Unit,
    onMangaClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
    onTopicClick: (Long) -> Unit,
    onUserClick: (Long) -> Unit
) {

    LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
        inProgressUserRatesPager(
            userRates = uiState.inProgressUserRates,
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick,
            onRateClick = onRateClick
        )


        animeOngoingCarousel(
            ongoingAnime = uiState.ongoings,
            onAnimeClick = onAnimeClick,
            onMoreClick = onExploreAnimeOngoingClick
        )

        newsTopicsHeader()
        newsTopicsFeed(
            newsTopics = uiState.newsTopics,
            onTopicClick = { onTopicClick(it.id) },
            onUserClick = { onUserClick(it.id) },
            key = { "$NewsTopicKeyPrefix${it.id}" }
        )
        allNewsTopicsButton(onAllNewsTopicsClick)
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
    onAnimeClick: (Long) -> Unit,
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
            ProvideTitleStringsByType(EntryType.Anime) {
                EntryCarousel {
                    animeCarouselItems(
                        animes = ongoingAnime,
                        onItemClick = { onAnimeClick(it.id) },
                        itemModifier = Modifier.width(OngoingAnimeWidth)
                    )
                }
            }
        }
    }
}

private fun LazyListScope.inProgressUserRatesPager(
    userRates: ImmutableList<UserRateWithEntry>,
    onAnimeClick: (Long) -> Unit,
    onMangaClick: (Long) -> Unit,
    onRateClick: (Long, TitleReference, UserRateValues, UserRateContext) -> Unit,
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
                val userRate = userRates[it]

                UserRateItem(
                    userRateWithEntry = userRate,
                    onAnimeClick = { onAnimeClick(it.id) },
                    onMangaClick = { onMangaClick(it.id) },
                    onEditClick = {
                        userRate.anime?.let { anime ->
                            onRateClick(
                                userRate.userRate.id,
                                anime.titleReference(),
                                userRate.userRate.toUserRateValues(),
                                anime.extractRateContext())
                        }

                        userRate.manga?.let { manga ->
                            onRateClick(
                                userRate.userRate.id,
                                manga.titleReference(),
                                userRate.userRate.toUserRateValues(),
                                manga.extractRateContext())
                        }
                    }
                )
            }
        }
    }
}

private const val InProgressUserRatesKey = "user_rates"
private const val OngoingAnimesKey = "ongoing"
private const val NewsHeaderKey = "news_header"
private const val NewsTopicKeyPrefix = "news"

private val OngoingAnimeWidth = 128.dp