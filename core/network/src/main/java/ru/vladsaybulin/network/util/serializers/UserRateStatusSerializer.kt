package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.STRING
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.asUserRateStatus

class UserRateStatusSerializer : KSerializer<UserRateStatus> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("userRateStatus", STRING)

    override fun deserialize(decoder: Decoder): UserRateStatus =
        decoder.decodeString().asUserRateStatus()

    override fun serialize(encoder: Encoder, value: UserRateStatus) {
        encoder.encodeString(value.serializedName)
    }

}