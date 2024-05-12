package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.textprocessor.html.HtmlToSeanimeTextTransformer
import ru.vladsaybulin.core.textprocessor.html.SeanimeTextBuilder
import ru.vladsaybulin.core.textprocessor.util.toHtmlDocument
import ru.vladsaybulin.database.models.text.ProtoReadyStyleValue
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextRange
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextSpoilerBlockItem
import ru.vladsaybulin.database.models.text.ProtoSeanimeTextStyle
import ru.vladsaybulin.database.models.text.SeanimeTextPOJO
import ru.vladsaybulin.model.annotatedtext.SeanimeText
import ru.vladsaybulin.model.annotatedtext.SeanimeText.SpoilerBlockItem
import ru.vladsaybulin.model.annotatedtext.SeanimeText.Style

fun String.asSeanimeText(): SeanimeText {
    val builder = SeanimeTextBuilder()
    HtmlToSeanimeTextTransformer.transform(this.toHtmlDocument(), builder)
    return builder.toSeanimeText()
}

fun SeanimeText.asSeanimeTextPOJO() = SeanimeTextPOJO(
    text = text,
    styles = styles.map { range -> range.asProtoModel(Style::asProtoModel) },
    spoilerBlocks = spoilerBlocks.map { range -> range.asProtoModel(SpoilerBlockItem::asProtoModel) },
    inlineSpoilers = inlineSpoilers.map { range -> range.asProtoModel { } },
    links = links.map { range -> range.asProtoModel { it } }
)

private fun <T, R> SeanimeText.Range<T>.asProtoModel(mapItem: (T) -> R) =
    ProtoSeanimeTextRange(
        start = start,
        end = end,
        tag = tag,
        item = mapItem(item)
    )

private fun Style.asProtoModel() = when (this) {
    is Style.ReadyStyle -> ProtoSeanimeTextStyle.ReadyStyle(value.asProtoReadyStyle())
}

private fun SeanimeText.ReadyStyleValue.asProtoReadyStyle() = when (this) {
    SeanimeText.ReadyStyleValue.H1 -> ProtoReadyStyleValue.H1
    SeanimeText.ReadyStyleValue.H2 -> ProtoReadyStyleValue.H2
    SeanimeText.ReadyStyleValue.H3 -> ProtoReadyStyleValue.H3
    SeanimeText.ReadyStyleValue.H4 -> ProtoReadyStyleValue.H4
    SeanimeText.ReadyStyleValue.H5 -> ProtoReadyStyleValue.H5
    SeanimeText.ReadyStyleValue.H6 -> ProtoReadyStyleValue.H6
    SeanimeText.ReadyStyleValue.Underline -> ProtoReadyStyleValue.UNDERLINE
    SeanimeText.ReadyStyleValue.Strikethrough -> ProtoReadyStyleValue.STRIKETHROUGH
    SeanimeText.ReadyStyleValue.Bold -> ProtoReadyStyleValue.BOLD
    SeanimeText.ReadyStyleValue.Italic -> ProtoReadyStyleValue.ITALIC
}

private fun SpoilerBlockItem.asProtoModel() = when (this) {
    SpoilerBlockItem.Block -> ProtoSeanimeTextSpoilerBlockItem.Block
    SpoilerBlockItem.Title -> ProtoSeanimeTextSpoilerBlockItem.Title
}