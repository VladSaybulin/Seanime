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

package ru.vladsaybulin.data.model

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import ru.vladsaybulin.database.models.anime.AnimeEntity
import ru.vladsaybulin.database.models.manga.MangaEntity
import ru.vladsaybulin.database.models.topic.TopicEntity
import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.model.topic.TopicLinkedType
import ru.vladsaybulin.model.topic.TopicLinkedType.Anime
import ru.vladsaybulin.model.topic.TopicLinkedType.Manga
import ru.vladsaybulin.model.topic.TopicLinkedType.Ranobe
import ru.vladsaybulin.network.models.anime.NetworkAnime
import ru.vladsaybulin.network.models.manga.NetworkManga
import ru.vladsaybulin.network.models.forum.NetworkTopic

fun NetworkTopic.asEntity() = TopicEntity(
    id = id,
    title = title,
    body = body,
    bodyHtml = bodyHtml,
    footerHtml = footerHtml,
    createdAt = createdAt,
    commentsCount = commentsCount,
    forumPermalink = forum.permalink,
    userId = user.id,
    type = type,
    linkedType = linkedType,
    animeId = getLinkedId(Anime),
    mangaId = getLinkedId(Manga) ?: getLinkedId(Ranobe),
    viewed = viewed ?: false,
    lastCommentViewed = lastCommentViewed ?: false,
    event = event,
    episode = episode
)

private fun NetworkTopic.getLinkedId(expectedType: TopicLinkedType) =
    if (linked != null && linkedType == expectedType) linkedId else null

fun NetworkTopic.userEntityShell(): UserEntity = user.asExternalModel()

fun NetworkTopic.linkedAnimeEntityShell(json: Json): AnimeEntity? {
    if (linkedType != Anime || linked == null) return null
    return json.decodeFromJsonElement<NetworkAnime>(linked!!).asEntity()
}

fun NetworkTopic.linkedMangaEntityShell(json: Json): MangaEntity? {
    if (linkedType != Manga && linkedType != Ranobe || linked == null) return null
    return json.decodeFromJsonElement<NetworkManga>(linked!!).asEntity()
}

