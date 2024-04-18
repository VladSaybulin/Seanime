package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.topic.TopicType
import ru.vladsaybulin.model.topic.asTopicType

class TopicTypeSerializer : KSerializer<TopicType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "topic_type",
        PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): TopicType = decoder.decodeString().asTopicType()

    override fun serialize(encoder: Encoder, value: TopicType) {
        encoder.encodeString(value.serializedValue)
    }

}