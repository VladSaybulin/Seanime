package ru.vladsaybulin.feature.imageview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.core.ui.drawForegroundGradientScrim

@Composable
fun ImageViewRoute(
    viewModel: ImageViewViewModel,
    onBack: () -> Unit,
) {
    ImageViewScreen(
        images = viewModel.requireImages,
        initialImage = viewModel.initialImage,
        onBack = onBack
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ImageViewScreen(
    images: ImmutableList<Image>,
    initialImage: Int,
    onBack: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = initialImage,
        pageCount = images::size
    )

    var isVisibleUi by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        HorizontalPager(
            state = pagerState,
            pageSpacing = 4.dp,
            beyondBoundsPageCount = 0,
            modifier = Modifier.fillMaxSize()
        ) { index ->
            val zoomState = rememberZoomState(maxScale = 5f)

            AsyncImage(
                model = images[index].originalUrl,
                contentDescription = null,
                onSuccess = { state ->
                    zoomState.setContentSize(state.painter.intrinsicSize)
                },
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .zoomable(
                        zoomState = zoomState,
                        onDoubleTap = { offset ->
                            val scale = zoomState.scale
                            val targetScale = when {
                                scale < 2.5f -> 2.5f
                                scale < 5f -> 5f
                                else -> 1f
                            }
                            zoomState.changeScale(targetScale, offset)
                        }
                    )
            )
        }

        AnimatedVisibility(
            visible = isVisibleUi,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .drawForegroundGradientScrim(
                            startColor = Color.Black.copy(alpha = 0.5f),
                            stopColor = Color.Black.copy(alpha = 0.0f),
                            decay = 1f
                        )
                )
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(
                                id = R.string.current_page_of_page_count,
                                pagerState.currentPage + 1,
                                pagerState.pageCount
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(imageVector = ShikimoriIcons.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White
                    )
                )
            }
        }
    }

}