package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.model.annotatedtext.AnnotatedText
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.Annotation
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.TextRange
import ru.vladsaybulin.model.annotatedtext.AnnotatedText.TextStyle

class AnnotatedTextBuilder : Appendable {

    private data class MutableTextRange<T: Any>(
        val start: Int,
        val item: T,
        var end: Int = Int.MIN_VALUE
    ) {
        fun toTextRange(defaultEnd: Int): TextRange<T> {
            val end = if (end == Int.MIN_VALUE) defaultEnd else end
            return TextRange(start, end, item)
        }
    }

    private val text = StringBuilder()
    private val styles: MutableList<MutableTextRange<TextStyle>> = mutableListOf()
    private val annotations: MutableList<MutableTextRange<Annotation>> = mutableListOf()

    private val stack: MutableList<MutableTextRange<out Any>> = mutableListOf()

    fun toAnnotatedText(): AnnotatedText = AnnotatedText(
        text = text.toString(),
        styles = styles.map { it.toTextRange(text.length) },
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

    fun pushStyle(style: TextStyle): Int {
        MutableTextRange(text.length, style).apply {
            styles.add(this)
            stack.add(this)
        }
        return stack.size - 1
    }

    fun pushAnnotation(tag: String, annotation: String): Int {
        MutableTextRange(text.length, Annotation(tag, annotation)).apply {
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

fun AnnotatedTextBuilder.withStyle(style: TextStyle, block: AnnotatedTextBuilder.() -> Unit) {
    val index = pushStyle(style)
    block()
    pop(index)
}

fun AnnotatedTextBuilder.withStyleAndAnnotation(
    style: TextStyle,
    tag: String,
    annotation: String,
    block: AnnotatedTextBuilder.() -> Unit
) {
    val index = pushStyle(style)
    pushAnnotation(tag, annotation)
    block()
    pop(index)
}