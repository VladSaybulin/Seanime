package ru.vladsaybulin.core.textprocessor.html

import ru.vladsaybulin.core.textprocessor.TagTransformer
import ru.vladsaybulin.core.textprocessor.util.htmlClasses

/**
 * Filter tags with name-en classes
 */
object LocalizedNameTransformer : TagTransformer<AnnotatedTextBuilder> {

    override fun transform(chain: TagTransformer.Chain<AnnotatedTextBuilder>) {
        val htmlClasses = chain.tagNode.htmlClasses()
        if (htmlClasses == null) {
            chain.proceed()
            return
        }

        if (htmlClasses.contains("name-ru")) {
            chain.transformChildren()
        }
    }
}