package ru.vladsaybulin.network.util.serializers

import kotlinx.datetime.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate

class LocalDateToIncompleteDateSerializer : KSerializer<NetworkIncompleteDate?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "incomplete_date",
        kind = PrimitiveKind.STRING
    )

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): NetworkIncompleteDate? {
        val localDate = decoder.decodeNullableSerializableValue(LocalDate.serializer())
            ?: return null
        val day = localDate.dayOfMonth.takeIf { it != 1 }
        val month = localDate.monthNumber.takeIf { day != null || it != 1 }

        return NetworkIncompleteDate(
            day = day,
            month = month,
            year = localDate.year
        )
    }

    override fun serialize(encoder: Encoder, value: NetworkIncompleteDate?) {
        throw UnsupportedOperationException()
    }
}