/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.textprocessor

import org.primeframework.transformer.domain.BaseTagNode
import org.primeframework.transformer.domain.Document
import org.primeframework.transformer.domain.Node
import org.primeframework.transformer.domain.TagNode
import org.primeframework.transformer.domain.TextNode

open class DocumentTransformer<Builder : Appendable>(tagTransformers: List<TagTransformer<Builder>>) {

    inner class TagTransformerScopeImpl(
        private val tagNode: TagNode,
        private val defaultBuilder: Builder
    ) : TagTransformerScope<Builder> {

        override fun transformChildren(builder: Builder?) {
            transformBaseTagNodeChildren(tagNode, builder ?: defaultBuilder)
        }

        override fun transformChildren(
            textNodeBuilder: Builder?,
            builderProvider: (TagNode) -> Builder?
        ) {
            for (child in tagNode.children) {
                transformNode(child, builderProvider, textNodeBuilder)
            }
        }
    }

    private val tagTransformers = tagTransformers.toMapOfTagTransformers()

    fun transform(document: Document, builder: Builder) {
        transformBaseTagNodeChildren(document, builder)
    }

    private fun transformBaseTagNodeChildren(
        tagNode: BaseTagNode,
        builder: Builder
    ) {
        tagNode.children.forEach {
            transformNode(node = it, builder = builder)
        }
    }

    private fun transformNode(node: Node, builder: Builder) {
        when (node) {
            is TextNode -> builder.append(node.body)
            is TagNode -> transformTagNode(node, builder)
        }
    }

    private fun transformNode(node: Node, builderProvider: (TagNode) -> Builder?, textNodeBuilder: Builder?) {
        when (node) {
            is TextNode -> textNodeBuilder?.append(node.body)
            is TagNode -> builderProvider(node)?.apply { transformTagNode(node, this) }
        }
    }

    private fun transformTagNode(
        tagNode: TagNode,
        builder: Builder
    ) {
        val transformers = tagTransformers[tagNode.name]
        if (transformers.isNullOrEmpty()) {
            transformBaseTagNodeChildren(tagNode, builder)
        } else {
            transformTagNodeWithTransformers(tagNode, builder, transformers)
        }
    }

    private fun transformTagNodeWithTransformers(
        tagNode: TagNode,
        builder: Builder,
        tagTransformers: List<TagTransformer<Builder>>,
    ) {
        val scope = TagTransformerScopeImpl(tagNode, builder)

        val iterator = tagTransformers.iterator()
        var lastResult: TagTransformerResult
        do {
            val tagTransformer = iterator.next()
            lastResult = with(tagTransformer) {
                scope.transform(tagNode, builder)
            }
        } while (iterator.hasNext() && lastResult == TagTransformerResult.NotTransformed)

        if (lastResult != TagTransformerResult.Success) {
            transformBaseTagNodeChildren(
                tagNode = tagNode,
                builder = builder
            )
        }
    }
}

internal fun <Builder> List<TagTransformer<Builder>>.toMapOfTagTransformers(): Map<String, List<TagTransformer<Builder>>> {
    if (isEmpty()) return emptyMap()
    return buildMap<String, MutableList<TagTransformer<Builder>>> {
        this@toMapOfTagTransformers.forEach { tagTransformer ->
            tagTransformer.tagNames.forEach { tagName ->
                val list = get(tagName)
                if (list == null) {
                    put(tagName, mutableListOf(tagTransformer))
                } else {
                    list.add(tagTransformer)
                }
            }
        }
        for (list in values) {
            list.sortByDescending { it.priority }
        }
    }
}