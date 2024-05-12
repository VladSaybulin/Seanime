package ru.vladsaybulin.core.textprocessor

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.primeframework.transformer.domain.Document
import org.primeframework.transformer.service.BBCodeParser
import ru.vladsaybulin.core.textprocessor.testdoubles.AlwaysNotTransformedSimpleTagTransformer
import ru.vladsaybulin.core.textprocessor.testdoubles.BuilderProviderTagTransformer
import ru.vladsaybulin.core.textprocessor.testdoubles.IgnoreTextNodesTagTransformer
import ru.vladsaybulin.core.textprocessor.testdoubles.SimpleTagTransformer

class DocumentTransformerTest {

    private lateinit var document: Document

    @Before
    fun createDocument() {
        document = BBCodeParser().buildDocument(
            """
            |Regular
            |[simple]simple content[/simple]
            |[ignore_children]
                |[nested]Ignore me[/nested]
                |Ignore me
            |[/ignore_children]
            |[ignoring_text_nodes]
                |Ignore me
                |[append_me]Append me[/append_me]
            |[/ignoring_only_text_nodes]
            ||[override_builders]
                |[separate_me]I should be last[/separate_me]
                |[append_me]Append me[/append_me]
                |[ignore_me]Ignore me[/ignore_me]
                |Append me
            |[/override_builders]
        """.trimMargin(), null
        )
    }

    @Test
    fun documentTransformer_transform_when_SimpleTagTransformer_is_present() {
        val builder = StringBuilder()
        val docTransformer = DocumentTransformer(listOf(SimpleTagTransformer()))

        docTransformer.transform(
            BBCodeParser().buildDocument("[simple]Text[/simple]", null),
            builder
        )

        assertEquals("[Text]", builder.toString())
    }

    @Test
    fun documentTransformer_transform_when_SimpleTagTransformer_is_not_present() {
        val builder = StringBuilder()
        val docTransformer = DocumentTransformer(emptyList<TagTransformer<StringBuilder>>())

        docTransformer.transform(
            BBCodeParser().buildDocument("[simple]Text[/simple]", null),
            builder
        )

        assertEquals("Text", builder.toString())
    }

    @Test
    fun documentTransformer_transform_simple_tag_when_AlwaysNotTransformedSimpleTagTransformer_is_priority() {
        val builder = StringBuilder()
        val docTransformer = DocumentTransformer(
            listOf(
                SimpleTagTransformer(),
                AlwaysNotTransformedSimpleTagTransformer(priority = 1)
            )
        )

        docTransformer.transform(
            BBCodeParser().buildDocument("[simple]Text[/simple]", null),
            builder
        )

        assertEquals("NT[Text]", builder.toString())
    }

    @Test
    fun documentTransformer_transform_when_BuilderProviderTagTransformer_is_not_present() {
        val builder = StringBuilder()
        val docTransformer = DocumentTransformer(listOf(BuilderProviderTagTransformer()))

        val documentSource = "[builder_provider][ignore_me]1[/ignore_me][defer_me]2[/defer_me]3[append_me]4[/append_me][/builder_provider]"
        docTransformer.transform(
            BBCodeParser().buildDocument(documentSource, null),
            builder
        )

        assertEquals("342", builder.toString())
    }

    @Test
    fun documentTransformer_transform_when_IgnoreTextNodesTagTransformer_is_not_present() {
        val builder = StringBuilder()
        val docTransformer = DocumentTransformer(listOf(IgnoreTextNodesTagTransformer()))

        docTransformer.transform(
            BBCodeParser().buildDocument("[ignore_text_nodes][nested]1[/nested]2[nested]3[/nested][/ignore_text_nodes]", null),
            builder
        )

        assertEquals("13", builder.toString())
    }
}