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

@file:OptIn(ExperimentalSerializationApi::class)

package ru.vladsaybulin.database.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextRange
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextSpoilerBlockItem
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextStyle
import java.util.Base64
import javax.inject.Inject

@ProvidedTypeConverter
class TextRangesTypeConverter @Inject constructor(private val protoBuf: ProtoBuf) {

    @Serializable
    data class ListOfRanges<T>(
        val ranges: List<ProtoSeanimeTextRange<T>>
    )

    @TypeConverter
    fun stylesToString(value: List<ProtoSeanimeTextRange<ProtoSeanimeTextStyle>>): String? =
        rangesToString(value)

    @TypeConverter
    fun stringToStyles(value: String?): List<ProtoSeanimeTextRange<ProtoSeanimeTextStyle>> =
        stringToRanges(value)

    @TypeConverter
    fun spoilerBlocksToString(value: List<ProtoSeanimeTextRange<ProtoSeanimeTextSpoilerBlockItem>>): String? =
        rangesToString(value)

    @TypeConverter
    fun stringToSpoilerBlocks(value: String?): List<ProtoSeanimeTextRange<ProtoSeanimeTextSpoilerBlockItem>> =
        stringToRanges(value)

    @TypeConverter
    fun inlineSpoilersToString(value: List<ProtoSeanimeTextRange<Unit>>): String? =
        rangesToString(value)

    @TypeConverter
    fun stringToInlineSpoilers(value: String?): List<ProtoSeanimeTextRange<Unit>> =
        stringToRanges(value)

    @TypeConverter
    fun linksToString(value: List<ProtoSeanimeTextRange<String>>): String? =
        rangesToString(value)

    @TypeConverter
    fun stringToLinks(value: String?): List<ProtoSeanimeTextRange<String>> =
        stringToRanges(value)

    private inline fun <reified T> stringToRanges(value: String?): List<ProtoSeanimeTextRange<T>> {
        if (value == null) return emptyList()
        val bytes = Base64.getDecoder().decode(value)
        return protoBuf.decodeFromByteArray<ListOfRanges<T>>(bytes).ranges
    }

    private inline fun <reified T> rangesToString(value: List<ProtoSeanimeTextRange<T>>): String? {
        if (value.isEmpty()) return null
        val bytes = protoBuf.encodeToByteArray<ListOfRanges<T>>(ListOfRanges(value))
        return Base64.getEncoder().encodeToString(bytes)
    }
}