package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.TagNode

class TagTransformerChain<Builder>(
    val tagNode: TagNode,
    val builder: Builder,
    private val tagTransformers: List<TagTransformer<Builder>>,
    private val index: Int = 0,
    private val transformChildren: (TagNode, Builder) -> Unit
) {
    private var proceedCalled = false
    private var transformChildrenCalled = false

    private fun copy(
        tagTransformers: List<TagTransformer<Builder>> = this.tagTransformers,
        index: Int = this.index,
        builder: Builder = this.builder,
        transformChildren: (TagNode, Builder) -> Unit = this.transformChildren
    ) = TagTransformerChain(tagNode, builder, tagTransformers, index, transformChildren)

    fun proceed() {
        check(!proceedCalled)
        proceedCalled = true
        if (index == tagTransformers.size) {
            transformChildren(tagNode, builder)
        }

        val next = copy(index = index + 1)
        tagTransformers[index + 1].transform(next)
    }

    fun transformChildren() {
        check(!transformChildrenCalled)
        transformChildrenCalled =true
        this.transformChildren.invoke(tagNode, builder)
    }
}