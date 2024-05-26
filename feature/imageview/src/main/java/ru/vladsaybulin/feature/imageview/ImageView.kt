package ru.vladsaybulin.feature.imageview

import android.app.Activity
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.vladsaybulin.core.designsystem.components.drawBackgroundGradientScrim
import ru.vladsaybulin.core.designsystem.icons.ShikimoriIcons
import ru.vladsaybulin.model.common.Image

sealed class ImageSet {
    data object NoSet : ImageSet()

    data class Set(val images: List<Image>, val initialImageIndex: Int) : ImageSet()
}

@Composable
fun ImageView(
    set: ImageSet,
    onBackClick: () -> Unit
) {
    when (set) {
        ImageSet.NoSet -> Unit
        is ImageSet.Set -> ImageContent(state = set, onBackClick = onBackClick)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ImageContent(
    state: ImageSet.Set,
    onBackClick: () -> Unit
) {

    val pagerState = rememberPagerState(
        initialPage = state.initialImageIndex,
        pageCount = state.images::size
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
            modifier = Modifier.fillMaxSize()
        ) { index ->
            ZoomableImage(
                image = state.images[index],
                onTap = { isVisibleUi = !isVisibleUi }
            )
        }

        SystemBarsVisible(visible = isVisibleUi)
        AnimatedVisibility(
            visible = isVisibleUi,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            ImageViewTopBar(
                currentPage = pagerState.currentPage,
                pageCount = pagerState.pageCount,
                isSingle = state.images.size == 1,
                onBackClick = onBackClick
            )
        }
    }
}

@Composable
private fun SystemBarsVisible(visible: Boolean) {
    val window = with(LocalContext.current as Activity) { return@with window }
    val controller = remember { WindowCompat.getInsetsController(window, window.decorView) }

    LaunchedEffect(controller, visible) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (visible) {
            controller.show(WindowInsetsCompat.Type.systemBars())
        } else {
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    DisposableEffect(controller) {
        onDispose {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            controller.show(WindowInsetsCompat.Type.systemBars())
        }
    }
}

@Composable
private fun ZoomableImage(
    image: Image,
    onTap: (Offset) -> Unit
) {
    val zoomState = rememberZoomState(maxScale = 5f)

    AsyncImage(
        model = image.originalUrl,
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
                },
                onTap = onTap
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageViewTopBar(
    currentPage: Int,
    pageCount: Int,
    isSingle: Boolean,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = {
            if (!isSingle) {
                Text(
                    text = stringResource(
                        id = R.string.current_page_of_page_count,
                        currentPage + 1,
                        pageCount
                    )
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = ShikimoriIcons.ArrowBack, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.White
        ),
        modifier = Modifier.drawBackgroundGradientScrim(
            topColor = Color.Black.copy(alpha = 0.5f),
            bottomColor = Color.Black.copy(alpha = 0.0f),
            decay = 1f
        )
    )
}