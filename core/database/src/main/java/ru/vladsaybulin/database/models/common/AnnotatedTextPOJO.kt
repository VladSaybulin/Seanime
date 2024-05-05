package ru.vladsaybulin.database.models.common

import androidx.room.ColumnInfo
import ru.vladsaybulin.model.annotatedtext.AnnotatedText

data class AnnotatedTextPOJO(

    @ColumnInfo("text")
    val text: String,

    @ColumnInfo("annotations")
    val annotations: List<ProtoTextAnnotation>
)

fun AnnotatedTextPOJO.asExternalModel() = AnnotatedText(
    text = text,
    annotations = annotations.map(ProtoTextAnnotation::asExternalModel)
)