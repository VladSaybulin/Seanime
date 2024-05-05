package ru.vladsaybulin.core.textprocessor

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.vladsaybulin.core.textprocessor.html.AnnotatedTextBuilder
import ru.vladsaybulin.core.textprocessor.html.DefaultHtmlToAnnotatedTextTagTransformers
import ru.vladsaybulin.core.textprocessor.html.HtmlToAnnotatedTextTransformer
import ru.vladsaybulin.core.textprocessor.util.toHtmlDocument
import ru.vladsaybulin.model.annotatedtext.AnnotatedText
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.Annotation

class HtmlToAnnotatedTextTest {

    lateinit var transformer: HtmlToAnnotatedTextTransformer

    @Before
    fun setup() {
        transformer = HtmlToAnnotatedTextTransformer(DefaultHtmlToAnnotatedTextTagTransformers)
    }

    @Test
    fun transformExternalLink() {
        val doc = "Это <a href=\"https://shikimori.one\">ссылка на сайт</a>".toHtmlDocument()
        val builder = AnnotatedTextBuilder()

        transformer.transform(doc, builder)

        val expected = AnnotatedText(
            text = "Это ссылка на сайт",
            annotations = listOf(
                Annotation(
                    start = 4,
                    end = 18,
                    tag = "url",
                    annotation = "https://shikimori.one"
                )
            )
        )

        assertEquals(expected, builder.toAnnotatedText())
    }

    @Test
    fun transformAnimeLink() {
        val doc =
            "Лучшее аниме: <a href=\"https://shikimori.one/animes/z20-naruto\" class=\"b-link bubbled-processed\" data-tooltip_url=\"https://shikimori.one/animes/z20-naruto/tooltip\" data-attrs=\"{&quot;id&quot;:20,&quot;type&quot;:&quot;anime&quot;,&quot;name&quot;:&quot;Naruto&quot;,&quot;russian&quot;:&quot;Наруто&quot;}\"><span class=\"name-en\">Naruto</span><span class=\"name-ru\">Наруто</span></a>".toHtmlDocument()
        val builder = AnnotatedTextBuilder()

        transformer.transform(doc, builder)

        val expected = AnnotatedText(
            text = "Лучшее аниме: Наруто",
            annotations = listOf(
                Annotation(
                    start = 14,
                    end = 20,
                    tag = "anime",
                    annotation = "20"
                )
            )
        )

        assertEquals(expected, builder.toAnnotatedText())
    }

    @Test
    fun boldItalicUnderlineStrikethroughTextStyles() {
        val doc =
            "Test <strong>bold</strong> <em>italic</em> <u>underline</u> <del>strikethrough</del>".toHtmlDocument()
        val builder = AnnotatedTextBuilder()

        transformer.transform(doc, builder)

        val expected = AnnotatedText(
            text = "Test bold italic underline strikethrough",
            annotations = listOf(
                Annotation(
                    start = 5,
                    end = 9,
                    tag = "text_style",
                    annotation = "b"
                ),
                Annotation(
                    start = 10,
                    end = 16,
                    tag = "text_style",
                    annotation = "i"
                ),
                Annotation(
                    start = 17,
                    end = 26,
                    tag = "text_style",
                    annotation = "u"
                ),
                Annotation(
                    start = 27,
                    end = 40,
                    tag = "text_style",
                    annotation = "s"
                )
            )
        )

        assertEquals(expected, builder.toAnnotatedText())
    }

    @Test
    fun headerTextStyles() {
        val doc = "Заголовки: <h1>H1</h1><h2>H2</h2><h3>H3</h3><h4>H4</h4><h5>H5</h5><h6>H6</h6><div class=\"headline\">Headline</div><div class=\"midheadline\">MidHeadline</div>".toHtmlDocument()
        val builder = AnnotatedTextBuilder()

        transformer.transform(doc, builder)

        val expected = AnnotatedText(
            text = "Заголовки: H1\nH2\nH3\nH4\nH5\nH6\nHeadline\nMidHeadline\n",
            annotations = listOf(
                Annotation(
                    start = 11,
                    end = 14,
                    tag = "text_style",
                    annotation = "h1"
                ),
                Annotation(
                    start = 14,
                    end = 17,
                    tag = "text_style",
                    annotation = "h2"
                ),
                Annotation(
                    start = 17,
                    end = 20,
                    tag = "text_style",
                    annotation = "h3"
                ),
                Annotation(
                    start = 20,
                    end = 23,
                    tag = "text_style",
                    annotation = "h4"
                ),
                Annotation(
                    start = 23,
                    end = 26,
                    tag = "text_style",
                    annotation = "h5"
                ),
                Annotation(
                    start = 26,
                    end = 29,
                    tag = "text_style",
                    annotation = "h6"
                ),
                Annotation(
                    start = 29,
                    end = 38,
                    tag = "text_style",
                    annotation = "h5"
                ),
                Annotation(
                    start = 38,
                    end = 50,
                    tag = "text_style",
                    annotation = "h6"
                ),
            )
        )

        assertEquals(expected, builder.toAnnotatedText())
    }
}
