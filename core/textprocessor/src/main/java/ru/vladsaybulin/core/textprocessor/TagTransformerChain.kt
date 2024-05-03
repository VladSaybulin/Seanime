package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagNode

class TagTransformerChain<Builder>(
    val tagNode: TagNode,
    val builder: Builder,
    private val tagTransformers: List<TagTransformer<Builder>>,
    private val index: Int = 0,
    private val transformChildren: (List<Node>, Builder) -> Unit
) {
    private var proceedCalled = false
    private var transformChildrenCalled = false

    private fun copy(
        tagTransformers: List<TagTransformer<Builder>> = this.tagTransformers,
        index: Int = this.index,
        builder: Builder = this.builder,
        transformChildren: (List<Node>, Builder) -> Unit = this.transformChildren
    ) = TagTransformerChain(tagNode, builder, tagTransformers, index, transformChildren)

    fun proceed() {
        check(!proceedCalled)
        proceedCalled = true
        if (index == tagTransformers.size) {
            transformChildren()
        }

        val next = copy(index = index + 1)
        with(tagTransformers[index]) { next.transform() }
    }

    fun transformChildren() {
        transformChildren(tagNode.children)
    }

    fun transformChildren(children: List<Node>) {
        check(!transformChildrenCalled)
        transformChildrenCalled = true
        this.transformChildren.invoke(children, builder)
    }
}