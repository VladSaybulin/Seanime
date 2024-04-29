package ru.vladsaybulin.core.textprocessor

fun interface TagTransformer<Builder> {
    fun transform(chain: TagTransformerChain<Builder>)
}

