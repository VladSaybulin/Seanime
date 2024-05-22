package ru.vladsaybulin.core.textprocessor.html

import org.primeframework.transformer.domain.TagNode
import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.TagTransformerResult
import ru.vladsaybulin.core.textprocessor.TagTransformerScope
import ru.vladsaybulin.core.textprocessor.util.containsHtmlClass
import ru.vladsaybulin.model.annotatedtext.SeanimeText

object SpoilerBlockTransformer : TagTransformer<SeanimeTextBuilder> {

    override val tagNames: Set<String> = setOf("div")

    override fun TagTransformerScope<SeanimeTextBuilder>.transform(
        tagNode: TagNode,
        builder: SeanimeTextBuilder
    ): TagTransformerResult {
        if (!tagNode.containsHtmlClass("b-spoiler_block")) {
            return TagTransformerResult.NotTransformed
        }

        val bodyBuilder = SeanimeTextBuilder()
        val titleBuilder = SeanimeTextBuilder()

        transformChildren(null) { childTagNode ->
            when (childTagNode.name) {
                "span" -> if (titleBuilder.isEmpty()) titleBuilder else null
                "div" -> bodyBuilder
                else -> null
            }
        }

        builder.spoilerBlock(SeanimeText.SpoilerBlockItem.Block) {
            val titleText = titleBuilder.toSeanimeText()
            if (titleText.text.isNotEmpty() && titleText.text != "спойлер") {
                spoilerBlock(SeanimeText.SpoilerBlockItem.Title) {
                    append(titleText)
                }
            }
            append(bodyBuilder)
        }

        builder.append('\n')

        return TagTransformerResult.Success
    }
}