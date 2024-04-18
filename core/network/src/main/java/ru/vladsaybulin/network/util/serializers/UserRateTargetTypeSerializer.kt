package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.asEntryType

class UserRateTargetTypeSerializer : KSerializer<EntryType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "target_type",
        kind = PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): EntryType =
        decoder.decodeString().asEntryType()

    override fun serialize(encoder: Encoder, value: EntryType) {
        encoder.encodeString(
            value.serializedName.replaceFirstChar { it.uppercase() }
        )
    }

}