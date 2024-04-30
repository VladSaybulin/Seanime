package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.Document
import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.domain.TextNode

open class DocumentTransformer <Builder> (
    private val tagsTransformers: Map<String, List<TagTransformer<Builder>>>,
    val textAppender: Builder.(String) -> Unit
) {
    fun transform(document: Document, builder: Builder) {
        baseTagNodeTransform(document.children, builder)
    }

    private fun baseTagNodeTransform(children: List<Node>, builder: Builder) {
        children.forEach { node ->
            when (node) {
                is TextNode -> builder.textAppender(node.body)
                is TagNode -> {
                    val transformers = tagsTransformers[node.name]
                    if (transformers.isNullOrEmpty()) {
                        baseTagNodeTransform(node.children, builder)
                    } else {
                        TagTransformerChain(
                            tagNode = node,
                            builder = builder,
                            tagTransformers = transformers,
                            transformChildren = ::baseTagNodeTransform
                        )
                    }
                }
            }
        }
    }
}