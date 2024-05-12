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