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

package ru.vladsaybulin.network.mapper.enums

import ru.vladsaybulin.core.network.graphql.type.VideoKindEnum
import ru.vladsaybulin.model.anime.VideoKind

fun VideoKindEnum.asVideoKind() = when (this) {
    VideoKindEnum.pv -> VideoKind.Pv
    VideoKindEnum.character_trailer -> VideoKind.CharacterTrailer
    VideoKindEnum.cm -> VideoKind.Cm
    VideoKindEnum.op -> VideoKind.Op
    VideoKindEnum.ed -> VideoKind.Ed
    VideoKindEnum.op_ed_clip -> VideoKind.OpEdClip
    VideoKindEnum.clip -> VideoKind.Clip
    VideoKindEnum.episode_preview -> VideoKind.EpisodePreview
    else -> VideoKind.Other
}