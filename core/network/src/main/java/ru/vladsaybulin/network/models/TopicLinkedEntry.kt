package ru.vladsaybulin.network.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.network.util.serializers.EntryStatusSerializer

/**
 * It's object may be anime or manga
 */
@Serializable
class TopicLinkedEntry(
    @SerialName("id") val id: Long,
    @SerialName("name") val originalName: String,
    @SerialName("russian") val russianName: String,
    @SerialName("image") val poster: PosterDto?,
    @SerialName("kind") val kind: String, //It's AnimeKind or MangaKind
    @SerialName("status")
    @Serializable(EntryStatusSerializer::class)
    val status: EntryStatus,
    @SerialName("score") val score: Float,
    @SerialName("episodes") val episodes: Int,
    @SerialName("episodes_aired") val episodesAired: Int,
    @SerialName("chapters") val chapters: Int,
    @SerialName("volumes") val volumes: Int,
    @SerialName("aired_on") val airedOn: LocalDate?,
    @SerialName("released_on") val releasedOn: LocalDate?
)

