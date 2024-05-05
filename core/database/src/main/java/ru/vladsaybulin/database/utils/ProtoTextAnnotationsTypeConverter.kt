@file:OptIn(ExperimentalSerializationApi::class)

package ru.vladsaybulin.database.utils

import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber
import ru.vladsaybulin.database.models.common.ProtoTextAnnotation
import java.util.Base64

class ProtoTextAnnotationsTypeConverter {

    @Serializable
    @OptIn(ExperimentalSerializationApi::class)
    private class ListOfAnnotations(
        @ProtoNumber(1) val annotations: List<ProtoTextAnnotation>
    )

    @TypeConverter
    fun textAnnotationsToString(value: List<ProtoTextAnnotation>): String {
        val bytes = ProtoBuf.encodeToByteArray(ListOfAnnotations(value))
        return Base64.getEncoder().encodeToString(bytes)
    }

    @TypeConverter
    fun stringToTextAnnotations(value: String): List<ProtoTextAnnotation> {
        val bytes = Base64.getDecoder().decode(value)
        return ProtoBuf.decodeFromByteArray<ListOfAnnotations>(bytes).annotations
    }

}