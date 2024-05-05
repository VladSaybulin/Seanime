package ru.vladsaybulin.core.textprocessor.util

import org.primeframework.transformer.domain.Document
import org.primeframework.transformer.service.HTMLParser

fun String.toHtmlDocument(): Document =
    HTMLParser().buildDocument(this, emptyMap())