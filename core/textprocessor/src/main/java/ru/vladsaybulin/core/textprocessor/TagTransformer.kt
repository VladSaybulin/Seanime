package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.TagNode

interface TagTransformer<Builder> {

    val priority: Int
        get() = 0

    val tagNames: Set<String>

    fun TagTransformerScope<Builder>.transform(
        tagNode: TagNode,
        builder: Builder,
    ): TagTransformerResult

}

interface TagTransformerScope<Builder> {

    fun transformChildren(builder: Builder? = null)

    fun transformChildren(
        textNodeBuilder: Builder?,
        builderProvider: (TagNode) -> Builder?
    )
}