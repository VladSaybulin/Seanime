package ru.vladsaybulin.network.util.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.vladsaybulin.model.topic.TopicEvent
import ru.vladsaybulin.model.topic.asTopicEvent

class TopicEventSerializer : KSerializer<TopicEvent> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        serialName = "topic_event",
        kind = PrimitiveKind.STRING
    )

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): TopicEvent =
        decoder.decodeNullableSerializableValue(String.serializer()).asTopicEvent()

    override fun serialize(encoder: Encoder, value: TopicEvent) {
        encoder.encodeString(value.serializedValue)
    }


}