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

package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope

object ExternalLinkTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("a")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        val href = tagNode.attributes["href"] ?: return TagTransformerResult.NotTransformed

        builder.link(tag = "url", annotation = href) {
            transformChildren()
        }
        return TagTransformerResult.Success
    }
}