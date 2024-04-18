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