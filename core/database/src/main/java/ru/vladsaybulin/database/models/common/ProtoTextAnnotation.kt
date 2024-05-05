package ru.vladsaybulin.database.models.common

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ProtoTextAnnotation (
    @ProtoNumber(1) val start: Int,
    @ProtoNumber(2) val end: Int,
    @ProtoNumber(3) val tag: String,
    @ProtoNumber(4) val annotation: String
)

fun ProtoTextAnnotation.asExternalModel() = AnnotatedText.Annotation(
    start = start,
    end = end,
    tag = tag,
    annotation = annotation
)