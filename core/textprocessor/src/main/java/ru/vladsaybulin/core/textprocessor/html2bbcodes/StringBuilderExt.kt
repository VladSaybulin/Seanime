package ru.vladsaybulin.core.textprocessor.html2bbcodes

internal fun StringBuilder.buildBBCodeTag(
    name: String,
    attribute: String? = null,
    attributes: Map<String, String>? = null,
    appendCloseTag: Boolean = true,
    body: (() -> Unit)? = null,
) {
    append("[").append(name)
    if (attribute != null) {
        append("=").append(attribute)
    }
    if (!attributes.isNullOrEmpty()) {
        append(" ")
        attributes.forEach { (k, v) -> append(k).append("=").append(v) }
    }
    append("]")
    if (body != null) {
        body()
    }
    if (appendCloseTag) {
        append("[/").append(name).append("]")
    }
}