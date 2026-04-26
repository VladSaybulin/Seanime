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

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.core.textprocessor.util.htmlClasses

object ShikimoriLinkTransformer : TagTransformer<SeanimeTextBuilder> {

    private val json = Json

    override val tagNames: Set<String> = setOf("a")

    override val priority: Int = 1

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        if (tagNode.htmlClasses()?.contains("b-link") != true) {
            return TagTransformerResult.NotTransformed
        }

        val dataAttrsString = tagNode.attributes["data-attrs"]
            ?: return TagTransformerResult.NotTransformed

        val attrs = json.decodeFromString<JsonObject>(dataAttrsString.replace("&quot;", "\""))

        val type = (attrs["type"] as JsonPrimitive).content
        val id = (attrs["id"] as JsonPrimitive).content
        val finalId = when (type) {
            "comment" -> "${(attrs["user_id"] as JsonPrimitive).content};$id"
            else -> id
        }

        builder.link(
            tag = type,
            annotation = finalId
        ) {
            transformChildren()
        }
        return TagTransformerResult.Success
    }
}