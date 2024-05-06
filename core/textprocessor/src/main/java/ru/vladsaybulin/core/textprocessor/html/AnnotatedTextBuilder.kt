package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.model.annotatedtext.AnnotatedText
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.Annotation

class AnnotatedTextBuilder : Appendable, CharSequence {

    private data class MutableAnnotation(
        val start: Int,
        val tag: String,
        val annotation: String,
        var end: Int = Int.MIN_VALUE,
    ) {
        fun toTextRange(defaultEnd: Int): Annotation {
            val end = if (end == Int.MIN_VALUE) defaultEnd else end
            return Annotation(
                start = start,
                end = end,
                tag = tag,
                annotation = annotation
            )
        }
    }

    private val text = StringBuilder()
    private val annotations: MutableList<MutableAnnotation> = mutableListOf()

    private val stack: MutableList<MutableAnnotation> = mutableListOf()

    override val length: Int
        get() = text.length

    override fun get(index: Int): Char =
        text[index]

    override fun subSequence(startIndex: Int, endIndex: Int) =
        text.subSequence(startIndex, endIndex)

    fun toAnnotatedText(): AnnotatedText = AnnotatedText(
        text = text.toString(),
        annotations = annotations.map { it.toTextRange(text.length) }
    )

    override fun append(csq: CharSequence?): java.lang.Appendable {
        text.append(csq)
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): java.lang.Appendable {
        text.append(csq, start, end)
        return this
    }

    override fun append(c: Char): java.lang.Appendable {
        text.append(c)
        return this
    }

    fun pushAnnotation(tag: String, annotation: String): Int {
        MutableAnnotation(start = text.length, tag = tag, annotation = annotation).apply {
            annotations.add(this)
            stack.add(this)
        }
        return stack.size - 1
    }

    fun pop() {
        check(stack.isNotEmpty())
        stack.removeLast().run {
            end = text.length
        }
    }

    fun pop(index: Int) {
        check(stack.size > index)
        while (stack.lastIndex >= index) {
            pop()
        }
    }
}

fun AnnotatedTextBuilder.withAnnotation(
    tag: String,
    annotation: String,
    block: AnnotatedTextBuilder.() -> Unit
) {
    val index = pushAnnotation(tag, annotation)
    block()
    pop(index)
}