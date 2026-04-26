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

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vladsaybulin.core.designsystem.theme.SeanimeTheme
import ru.vladsaybulin.core.ui.text.SeanimeExpandableText
import ru.vladsaybulin.core.ui.text.onSeanimeTextLinkClickAdapter
import ru.vladsaybulin.model.annotatedtext.SeanimeText

@Composable
fun TitleDescription(
    description: SeanimeText,
    onAnimeClick: (Long) -> Unit?,
    onMangaClick: (Long) -> Unit?,
    onCharacterClick: (Long) -> Unit?,
    onPersonClick: (Long) -> Unit?,
    onUrlClick: (String) -> Unit?,
) {
    SeanimeExpandableText(
        text = description,
        style = SeanimeTheme.typography.bodyMedium,
        modifier = Modifier.padding(horizontal = 16.dp),
        onLinkClick = onSeanimeTextLinkClickAdapter(
            onAnimeClick = onAnimeClick,
            onMangaClick = onMangaClick,
            onCharacterClick = onCharacterClick,
            onPersonClick = onPersonClick,
            onUrlClick = onUrlClick,
        )
    )
}