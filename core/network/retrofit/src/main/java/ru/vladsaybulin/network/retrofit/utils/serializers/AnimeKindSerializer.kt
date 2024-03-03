package ru.vladsaybulin.network.retrofit.utils.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.asAnimeKind

internal class AnimeKindSerializer : KSerializer<AnimeKind> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("anime_kind", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AnimeKind =
        decoder.decodeString().asAnimeKind()

    override fun serialize(encoder: Encoder, value: AnimeKind) {
        encoder.encodeString(value.serializedName)
    }

}