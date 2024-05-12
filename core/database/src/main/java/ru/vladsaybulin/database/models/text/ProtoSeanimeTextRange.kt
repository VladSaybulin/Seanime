package ru.vladsaybulin.database.models.text

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import ru.vladsaybulin.model.annotatedtext.SeanimeText

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ProtoSeanimeTextRange<T>(
    @ProtoNumber(1) val start: Int,
    @ProtoNumber(2) val end: Int,
    @ProtoNumber(3) val tag: String,
    @ProtoNumber(4) val item: T
)

fun <T, K> ProtoSeanimeTextRange<T>.asExternalModel(
    mapItem: (T) -> K
) = SeanimeText.Range(
    start = start,
    end = end,
    tag = tag,
    item = mapItem(item)
)