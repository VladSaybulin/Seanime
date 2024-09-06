package ru.vladsaybulin.feature.title.videos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.LocalScreenContentPadding
import ru.vladsaybulin.feature.title.videos.navigation.AnimeVideosNavEvents
import ru.vladsaybulin.model.anime.Video
import ru.vladsaybulin.model.anime.VideoKind

@Composable
fun AnimeVideosRoute(
    navEvents: AnimeVideosNavEvents,
    viewModel: AnimeVideosViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AnimeVideosScreen(
        state = state,
        navEvents = navEvents
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeVideosScreen(
    state: AnimeVideosUIState,
    navEvents: AnimeVideosNavEvents
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            AnimeVideosTopBar(
                onBackClick = navEvents.navigateUp,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .padding(scaffoldPadding)
                .padding(LocalScreenContentPadding.current)
        ) {
            if (state is AnimeVideosUIState.Success) {
                AnimeVideosContent(
                    state = state,
                    onVideoClick = navEvents.navigateToVideo
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimeVideosTopBar(
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.feature_anime_videos_title)) },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = SeanimeIcons.ArrowBack,
                    contentDescription = stringResource(id = R.string.feature_anime_videos_back)
                )
            }
        }
    )
}

@Composable
private fun AnimeVideosContent(
    state: AnimeVideosUIState.Success,
    onVideoClick: (url: String, name: String?, kind: VideoKind) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = state.videos) { video ->
            VideoItem(video = video, onClick = { onVideoClick(video.videoUrl, video.name, video.kind) })
        }
    }
}

@Composable
private fun VideoItem(
    video: Video,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
    ) {
        Row {
            AsyncImage(
                model = video.previewImageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .width(144.dp)
                    .aspectRatio(16 / 9f)
                    .clip(SeanimeTheme.shapes.medium)
            )

            Column(
                modifier = Modifier.fillMaxHeight().padding(start = 16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = video.name ?: stringResource(id = R.string.feature_anime_videos_no_name),
                    style = SeanimeTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .background(
                            SeanimeTheme.colorScheme.surfaceColorAtElevation(4.dp),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            color = SeanimeTheme.colorScheme.outlineVariant,
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                ) {
                    Text(
                        text = videoKindStringResource(kind = video.kind),
                        style = SeanimeTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun videoKindStringResource(kind: VideoKind) = when (kind) {
    VideoKind.Pv -> R.string.feature_anime_videos_video_kind_pv
    VideoKind.CharacterTrailer -> R.string.feature_anime_videos_video_kind_character_trailer
    VideoKind.Cm -> R.string.feature_anime_videos_video_kind_cm
    VideoKind.Op -> R.string.feature_anime_videos_video_kind_op
    VideoKind.Ed -> R.string.feature_anime_videos_video_kind_ed
    VideoKind.OpEdClip -> R.string.feature_anime_videos_video_kind_op_ed_clip
    VideoKind.Clip -> R.string.feature_anime_videos_video_kind_clip
    VideoKind.Other -> R.string.feature_anime_videos_video_kind_other
    VideoKind.EpisodePreview -> R.string.feature_anime_videos_video_kind_episode_preview
}.let { stringResource(id = it) }

@Preview
@Composable
fun VideoItemPreview() {
    SeanimeTheme {
        VideoItem(
            video = Video(
                name = "Какое-то имя",
                previewImageUrl = "",
                videoUrl = "",
                playerUrl = "",
                kind = VideoKind.CharacterTrailer
            ),
            onClick = { }
        )
    }
}