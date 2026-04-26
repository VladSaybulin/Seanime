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

package ru.vladsaybulin.feature.title.details.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import ru.vladsaybulin.core.designsystem.components.drawForegroundGradientScrim
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme

@Composable
fun TitlePoster(
    posterUrl: String?,
    topSpace: Dp,
    onClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {

        val painter = if (posterUrl == null || LocalInspectionMode.current) {
            painterResource(id = ru.vladsaybulin.core.ui.R.drawable.no_poster)
        } else {
            rememberAsyncImagePainter(model = posterUrl)
        }

        if (posterUrl != null) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .blur(8.dp)
                    .drawForegroundGradientScrim(
                        topColor = SeanimeTheme.colorScheme.surface.copy(alpha = 0.8f),
                        bottomColor = SeanimeTheme.colorScheme.surface
                    )
            )
        }

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = topSpace)
                .fillMaxWidth(0.7f)
                .aspectRatio(3 / 4f)
                .align(Alignment.TopCenter)
                .shadow(4.dp, shape = SeanimeTheme.shapes.large, clip = true)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    enabled = posterUrl != null,
                    role = Role.Image,
                    onClick = onClick
                )
        )
    }
}