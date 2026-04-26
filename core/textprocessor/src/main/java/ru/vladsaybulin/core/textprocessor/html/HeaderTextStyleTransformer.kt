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
import ru.vladsaybulin.model.annotatedtext.SeanimeText

object HeaderTextStyleTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("h1", "h2", "h3", "h4", "h5", "h6")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        val readyStyleValue = when (tagNode.name) {
            "h1" -> SeanimeText.ReadyStyleValue.H1
            "h2" -> SeanimeText.ReadyStyleValue.H2
            "h3" -> SeanimeText.ReadyStyleValue.H3
            "h4" -> SeanimeText.ReadyStyleValue.H4
            "h5" -> SeanimeText.ReadyStyleValue.H5
            "h6" -> SeanimeText.ReadyStyleValue.H6
            else -> return TagTransformerResult.NotTransformed
        }

        builder.withStyle(SeanimeText.Style.ReadyStyle(readyStyleValue)) {
            if (get(length - 1) != '\n') {
                append("\n")
            }
            transformChildren()
            append("\n")
        }
        return TagTransformerResult.Success
    }
}