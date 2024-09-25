package ru.vladsaybulin.network.models.manga

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.network.models.userrate.NetworkUserRate
import ru.vladsaybulin.network.models.common.NetworkImage
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate
import ru.vladsaybulin.network.util.serializers.EntryStatusSerializer
import ru.vladsaybulin.network.util.serializers.LocalDateToIncompleteDateSerializer
import ru.vladsaybulin.network.util.serializers.MangaKindSerializer

@Serializable
data class NetworkManga(
    @SerialName("id") val id: Long,
    @SerialName("name") val originalName: String,
    @SerialName("russian") val russianName: String?,
    @SerialName("image") val poster: NetworkImage?,
    @SerialName("kind")
    @Serializable(MangaKindSerializer::class)
    val kind: MangaKind,
    @SerialName("status")
    @Serializable(EntryStatusSerializer::class)
    val status: EntryStatus,
    @SerialName("score") val score: Float?,
    @SerialName("chapters") val chapters: Int,
    @SerialName("volumes") val volumes: Int,
    @SerialName("aired_on")
    @Serializable(LocalDateToIncompleteDateSerializer::class)
    val airedOn: NetworkIncompleteDate?,
    @SerialName("released_on")
    @Serializable(LocalDateToIncompleteDateSerializer::class)
    val releasedOn: NetworkIncompleteDate?,

    //This field is not in the Rest API. Used for Graphql response
    @Transient val userRate: NetworkUserRate? = null
)