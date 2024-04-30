package ru.vladsaybulin.core.textprocessor.html2bbcodes

import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import ru.vladsaybulin.core.textprocessor.TagTransformer

private class DataAttrs(
    @SerialName("id") val id: Long,
    @SerialName("type") val type: String,
    @SerialName("userId") val userId: String? = null
)

internal val LinkWithDataAttrsTransformer = TagTransformer<StringBuilder> {
    val dataAttrsString = tagNode.attributes["data-attrs"]
    if (dataAttrsString != null) {
        val dataAttrs: DataAttrs = DataAttrsJson.decodeFromString(dataAttrsString)
        checkNotNull(dataAttrs.userId)
        builder.buildBBCodeTag(
            name = dataAttrs.type,
            attribute = when (dataAttrs.type) {
                "comment" -> "${dataAttrs.id};${dataAttrs.userId}"
                else -> "${dataAttrs.id}"
            },
            body = ::transformChildren
        )
    } else proceed()
}

internal val LinkTransformer = TagTransformer<StringBuilder> {
    val url = tagNode.attributes["href"]
    if (url != null) {
        builder.buildBBCodeTag(
            name = "url",
            attribute = url,
            body = ::transformChildren
        )
    } else proceed()
}

private val DataAttrsJson = Json { ignoreUnknownKeys = true }