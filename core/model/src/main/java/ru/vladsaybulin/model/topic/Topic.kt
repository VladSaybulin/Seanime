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

package ru.vladsaybulin.model.topic

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.user.BriefUser

data class Topic(
    val id: Long,
    val title: String,
    val body: String?,
    val bodyHtml: String?,
    val footerHtml: String?,
    val createdAt: Instant,
    val commentsCount: Int,
    val forumPermalink: String,
    val user: BriefUser,
    val type: TopicType,
    val linkedType: TopicLinkedType,
    val linkedEntry: TopicLinkedEntry?,
    val viewed: Boolean,
    val lastCommentViewed: Boolean,
    val event: TopicEvent,
    val episode: Int?
)