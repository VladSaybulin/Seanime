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

package ru.vladsaybulin.feature.imageview.impl

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import ru.vladsaybulin.core.designsystem.icons.SeanimeIcons
import ru.vladsaybulin.core.designsystem.theme.LocalSeanimeColors

@Composable
fun ImageViewScreen(
    viewModel: ImageViewViewModel,
    onBackClick: () -> Unit
) {
    val loadState by viewModel.loadState.collectAsStateWithLifecycle()

    ImageViewScreen(
        loadState = loadState,
        initialImageIndex = viewModel.initialIndex,
        onBackClick = onBackClick
    )
}

@Composable
fun ImageViewScreen(
    loadState: ImageViewLoadState,
    initialImageIndex: Int,
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black,
        contentColor = LocalSeanimeColors.current.onPosterScrim
    ) {
        val success = loadState as? ImageViewLoadState.Success

        var uiVisible by remember { mutableStateOf(false) }
        val pagerState = rememberPagerState { success?.images?.size ?: 0 }

        var started by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(success) {
            if (started || success == null) return@LaunchedEffect

            started = true
            pagerState.requestScrollToPage(initialImageIndex)
        }

        ImageViewDecoration(
            pageCount = pagerState.pageCount,
            currentPage = pagerState.currentPage,
            isLoading = loadState is ImageViewLoadState.Loading,
            uiVisible = uiVisible,
            onBackClick = onBackClick
        ) {
            if (success != null) {
                ImageViewPager(
                    loadState = success,
                    pagerState = pagerState,
                    onTap = { uiVisible = !uiVisible }
                )
            }
        }
    }
}

@Composable
private fun ImageViewDecoration(
    uiVisible: Boolean,
    pageCount: Int,
    currentPage: Int,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    SystemBarsVisibility(visible = uiVisible)

    Box {
        content()
        ImageViewTopBar(
            pageCount = pageCount,
            currentPage = currentPage,
            visible = uiVisible,
            onBackClick = onBackClick
        )

        if (isLoading) {
            LoadingImageIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImageViewTopBar(
    pageCount: Int,
    currentPage: Int,
    visible: Boolean,
    onBackClick: () -> Unit
) {
    val colors = LocalSeanimeColors.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        TopAppBar(
            title = {
                Text(stringResource(R.string.current_page_of_page_count, currentPage + 1, pageCount))
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = SeanimeIcons.ArrowBack,
                        contentDescription = null,
                        tint = colors.onPosterScrim
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colors.posterScrim.copy(alpha = 0.54f),
                navigationIconContentColor = colors.onPosterScrim,
                titleContentColor = colors.onPosterScrim
            ),
            windowInsets = WindowInsets.systemBars,
            expandedHeight = 48.dp
        )
    }
}

@Composable
private fun ImageViewPager(
    loadState: ImageViewLoadState.Success,
    pagerState: PagerState,
    onTap: () -> Unit
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        val image = loadState.images.get(page)
        ZoomableImage(
            url = image,
            onTap = onTap,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ZoomableImage(
    url: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val zoomState = rememberZoomState(maxScale = 5f)

    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier
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
                onTap = { onTap() }
            ),
    )
}

@Composable
private fun LoadingImageIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
private fun SystemBarsVisibility(visible: Boolean) {
    val controller = rememberWindowInsetsController()

    DisposableEffect(visible) {
        if (visible || controller == null) {
            onDispose {  }
        } else {
            controller.run {
                hide(InsetsTypesToHide)
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                onDispose {
                    show(InsetsTypesToHide)
                }
            }
        }
    }
}

@Composable
fun rememberWindowInsetsController(): WindowInsetsControllerCompat? {
    val activity = LocalActivity.current
    return remember(activity) {
        activity?.window?.let { WindowCompat.getInsetsController(it, it.decorView) }
    }
}

private val InsetsTypesToHide
    get() = WindowInsetsCompat.Type.systemBars() xor WindowInsetsCompat.Type.navigationBars()