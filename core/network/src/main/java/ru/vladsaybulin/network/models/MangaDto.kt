package ru.vladsaybulin.network.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.MangaKind
import ru.vladsaybulin.network.util.serializers.EntryStatusSerializer
import ru.vladsaybulin.network.util.serializers.MangaKindSerializer

@Serializable
data class MangaDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val originalName: String,
    @SerialName("russian") val russianName: String,
    @SerialName("image") val poster: PosterDto?,
    @SerialName("kind")
    @Serializable(MangaKindSerializer::class)
    val kind: MangaKind,
    @SerialName("status")
    @Serializable(EntryStatusSerializer::class)
    val status: EntryStatus,
    @SerialName("score") val score: Float,
    @SerialName("chapters") val chapters: Int,
    @SerialName("volumes") val volumes: Int,
    @SerialName("aired_on") val airedOn: LocalDate?,
    @SerialName("released_on") val releasedOn: LocalDate?
    )