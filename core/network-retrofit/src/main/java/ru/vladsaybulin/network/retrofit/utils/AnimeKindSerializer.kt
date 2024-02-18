package ru.vladsaybulin.network.retrofit.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.AnimeKind

internal class AnimeKindSerializer : KSerializer<AnimeKind> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("anime_kind", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): AnimeKind = decoder.decodeString().run {
        AnimeKind.entries.firstOrNull { it.serializedName == this@run } ?: AnimeKind.None
    }

    override fun serialize(encoder: Encoder, value: AnimeKind) {
        encoder.encodeString(value.serializedName)
    }

}