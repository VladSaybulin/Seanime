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

package ru.vladsaybulin.core.ui.text

fun <Action> onSeanimeTextLinkClickAdapter(
    onAnimeClick: (Long) -> Action?,
    onMangaClick: (Long) -> Action?,
    onCharacterClick: (Long) -> Action?,
    onPersonClick: (Long) -> Action?,
    onUrlClick: (String) -> Action?,
    onAction: (Action) -> Unit
): (String, String) -> Unit = { tag, annotation ->
    val action = when (tag) {
        "anime" -> onAnimeClick(annotation.toLong())
        "manga", "ranobe" -> onMangaClick(annotation.toLong())
        "character" -> onCharacterClick(annotation.toLong())
        "person" -> onPersonClick(annotation.toLong())
        "url" -> onUrlClick(annotation)
        else -> null
    }
    action?.let(onAction)
}

fun onSeanimeTextLinkClickAdapter(
    onAnimeClick: (Long) -> Unit?,
    onMangaClick: (Long) -> Unit?,
    onCharacterClick: (Long) -> Unit?,
    onPersonClick: (Long) -> Unit?,
    onUrlClick: (String) -> Unit?,
): (String, String) -> Unit = { tag, annotation ->
    when (tag) {
        "anime" -> onAnimeClick(annotation.toLong())
        "manga", "ranobe" -> onMangaClick(annotation.toLong())
        "character" -> onCharacterClick(annotation.toLong())
        "person" -> onPersonClick(annotation.toLong())
        "url" -> onUrlClick(annotation)
    }
}