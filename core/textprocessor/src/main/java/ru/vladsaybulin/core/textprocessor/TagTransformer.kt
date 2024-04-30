package ru.vladsaybulin.core.textprocessor

fun interface TagTransformer<Builder> {
    fun TagTransformerChain<Builder>.transform()
}

