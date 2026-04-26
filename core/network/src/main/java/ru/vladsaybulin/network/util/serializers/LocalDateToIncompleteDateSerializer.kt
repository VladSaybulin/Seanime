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