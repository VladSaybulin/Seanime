package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagNode

fun interface TagTransformer<Builder> {
    fun transform(chain: Chain<Builder>)

    interface Chain <Builder> {

        val tagNode: TagNode

        val builder: Builder

        fun proceed()

        fun transformChildren(children: List<Node> = tagNode.children)
    }
}

class TagTransformerChainImpl <Builder>(
    override val tagNode: TagNode,
    override val builder: Builder,
    private val transformers: List<TagTransformer<Builder>>,
    private val index: Int = 0,
    private val transformNodes: (children: List<Node>, builder: Builder) -> Unit
) : TagTransformer.Chain<Builder> {

    private var proceedCalled = false
    private var transformChildrenCalled = false

    private fun copy(
        tagNode: TagNode = this.tagNode,
        builder: Builder = this.builder,
        index: Int = this.index,
        transformers: List<TagTransformer<Builder>> = this.transformers,
        transformNodes: (children: List<Node>, builder: Builder) -> Unit = this.transformNodes
    ) = TagTransformerChainImpl(
        tagNode = tagNode,
        builder = builder,
        transformers = transformers,
        index = index,
        transformNodes = transformNodes
    )

    override fun proceed() {
        check(!proceedCalled) {
            "TagTransformer must call proceed() exactly once"
        }
        proceedCalled = true

        if (index + 1 >= transformers.size) {
            transformChildren()
            return
        }

        transformers[index + 1].transform(copy(index = this.index + 1))
    }

    override fun transformChildren(children: List<Node>) {
        check(!transformChildrenCalled) {
            "TagTransformer must call transformChildren(...) exactly once"
        }
        transformChildrenCalled = true

        transformNodes(children, builder)
    }
}
