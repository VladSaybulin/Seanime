package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.asEntryStatus

internal class EntryStatusSerializer : KSerializer<EntryStatus> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("entry_status", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): EntryStatus =
        decoder.decodeString().asEntryStatus()

    override fun serialize(encoder: Encoder, value: EntryStatus) {
        encoder.encodeString(value.serializedName)
    }
}