package ru.vladsaybulin.network.retrofit.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.EntryStatus

internal class EntryStatusSerializer : KSerializer<EntryStatus> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("entry_status", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): EntryStatus = decoder.decodeString().run {
        EntryStatus.entries.firstOrNull { it.serializedName == this@run } ?: EntryStatus.None
    }

    override fun serialize(encoder: Encoder, value: EntryStatus) {
        TODO("Not yet implemented")
    }
}