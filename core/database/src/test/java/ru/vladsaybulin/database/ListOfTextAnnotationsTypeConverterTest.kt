package ru.vladsaybulin.database

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.vladsaybulin.database.models.common.ProtoTextAnnotation
import ru.vladsaybulin.database.utils.ProtoTextAnnotationsTypeConverter

class ListOfTextAnnotationsTypeConverterTest {

    private lateinit var typeConverter: ProtoTextAnnotationsTypeConverter

    @Before
    fun setup() {
        typeConverter = ProtoTextAnnotationsTypeConverter()
    }

    @Test
    fun test() {
        val annotations = listOf(
            ProtoTextAnnotation(
                start = 4,
                end = 9,
                tag = "tag1",
                annotation = "annotation1"
            ),
            ProtoTextAnnotation(
                start = 10,
                end = 19,
                tag = "tag2",
                annotation = "annotation2"
            ),
            ProtoTextAnnotation(
                start = 21,
                end = 24,
                tag = "tag3",
                annotation = "annotation3"
            )
        )

        val serialized = typeConverter.textAnnotationsToString(annotations)
        val deserializedAnnotations = typeConverter.stringToTextAnnotations(serialized)

        assertEquals(annotations, deserializedAnnotations)
    }
}