package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.BaseTagNode
import org.primeframework.transformer.domain.Document
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.domain.TextNode

open class DocumentTransformer <Builder> (
    private val tagsTransformers: Map<String, List<TagTransformer<Builder>>>,
    val textAppender: Builder.(String) -> Unit
) {
    fun transform(document: Document, builder: Builder) {
        baseTagNodeTransform(document, builder)
    }

    private fun baseTagNodeTransform(baseTagNode: BaseTagNode, builder: Builder) {
        baseTagNode.children.forEach { node ->
            when (node) {
                is TextNode -> builder.textAppender(node.body)
                is TagNode -> {
                    val transformers = tagsTransformers[node.name]
                    if (transformers.isNullOrEmpty()) {
                        baseTagNodeTransform(node, builder)
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