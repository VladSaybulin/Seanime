package ru.vladsaybulin.core.textprocessor.html

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.util.htmlClasses

object ShikimoriLinkTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        if (chain.tagNode.htmlClasses()?.contains("b-link") != true) {
            chain.proceed()
            return
        }

        val dataAttrsString = chain.tagNode.attributes["data-attrs"]

        if (dataAttrsString == null) {
            chain.proceed()
            return
        }

        val attrs = json.decodeFromString<JsonObject>(dataAttrsString.replace("&quot;", "\""))

        val type = (attrs["type"] as JsonPrimitive).content
        val id = (attrs["id"] as JsonPrimitive).content
        val finalId = when (type) {
            "comment" -> "${(attrs["user_id"] as JsonPrimitive).content};$id"
            else -> id
        }

        chain.builder.withAnnotation(
            tag = type,
            annotation = finalId
        ) {
            chain.transformChildren()
        }
    }

    private val json = Json
}