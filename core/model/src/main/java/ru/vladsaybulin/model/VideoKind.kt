package ru.vladsaybulin.model

enum class VideoKind(val serializedName: String) {
    Pv("pv"),
    CharacterTrailer("character_trailer"),
    Cm("cm"),
    Op("op"),
    Ed("ed"),
    OpEdClip("op_ed_clip"),
    Clip("clip"),
    Other("other"),
    EpisodePreview("episode_preview"),
}