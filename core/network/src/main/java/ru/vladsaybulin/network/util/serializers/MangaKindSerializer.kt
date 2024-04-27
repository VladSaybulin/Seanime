package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.asMangaKind

class MangaKindSerializer : KSerializer<MangaKind> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "manga_kind",
        kind = PrimitiveKind.STRING
    )

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): MangaKind =
        decoder.decodeNullableSerializableValue(String.serializer()).asMangaKind()

    override fun serialize(encoder: Encoder, value: MangaKind) {
        encoder.encodeString(value.serializedName)
    }

}