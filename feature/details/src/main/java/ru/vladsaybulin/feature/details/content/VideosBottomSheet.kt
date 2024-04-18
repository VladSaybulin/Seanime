package ru.vladsaybulin.feature.details.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.components.ShikimoriModalBottomSheet
import ru.vladsaybulin.model.anime.Video

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun VideosBottomSheet(
    videos: List<Video>,
    onVideoClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    ShikimoriModalBottomSheet(onDismissRequest = onDismissRequest, modifier = modifier) {
        VideosBottomSheetContent(
            videos = videos,
            onVideoClick = onVideoClick
        )
    }
}

@Composable
private fun VideosBottomSheetContent(
    videos: List<Video>,
    onVideoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = videos) { video ->

        }
    }
}

@Composable
private fun VideoListItem(
    video: Video,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(

        ) {

        }
    }
}