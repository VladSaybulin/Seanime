package ru.vladsaybulin.core.textprocessor.util

import org.primeframework.transformer.domain.TagNode

val TagNode.rawBody: String
    get() {
        if (!hasBody()) return ""
        return document.getString(begin + bodyBegin, begin + bodyEnd)
    }

fun TagNode.htmlClasses() =
    attributes["class"]?.split(' ')