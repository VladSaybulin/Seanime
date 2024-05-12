package ru.vladsaybulin.database.models.text

import ru.vladsaybulin.model.annotatedtext.SeanimeText

enum class ProtoReadyStyleValue {
    H1,
    H2,
    H3,
    H4,
    H5,
    H6,
    UNDERLINE,
    STRIKETHROUGH,
    BOLD,
    ITALIC
}

fun ProtoReadyStyleValue.asExternalModel() = when (this) {
    ProtoReadyStyleValue.H1 -> SeanimeText.ReadyStyleValue.H1
    ProtoReadyStyleValue.H2 -> SeanimeText.ReadyStyleValue.H2
    ProtoReadyStyleValue.H3 -> SeanimeText.ReadyStyleValue.H3
    ProtoReadyStyleValue.H4 -> SeanimeText.ReadyStyleValue.H4
    ProtoReadyStyleValue.H5 -> SeanimeText.ReadyStyleValue.H5
    ProtoReadyStyleValue.H6 -> SeanimeText.ReadyStyleValue.H6
    ProtoReadyStyleValue.UNDERLINE -> SeanimeText.ReadyStyleValue.Underline
    ProtoReadyStyleValue.STRIKETHROUGH -> SeanimeText.ReadyStyleValue.Strikethrough
    ProtoReadyStyleValue.BOLD -> SeanimeText.ReadyStyleValue.Bold
    ProtoReadyStyleValue.ITALIC -> SeanimeText.ReadyStyleValue.Italic
}