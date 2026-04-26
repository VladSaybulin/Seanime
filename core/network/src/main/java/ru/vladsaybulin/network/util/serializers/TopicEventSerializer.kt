/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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