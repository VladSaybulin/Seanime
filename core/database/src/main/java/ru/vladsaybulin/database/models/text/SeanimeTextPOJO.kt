package ru.vladsaybulin.database.models.text

import androidx.room.ColumnInfo
import kotlinx.collections.immutable.toImmutableList
import ru.vladsaybulin.model.annotatedtext.SeanimeText

data class SeanimeTextPOJO(

    @ColumnInfo("text")
    val text: String,

    @ColumnInfo("styles")
    val styles: List<ProtoSeanimeTextRange<ProtoSeanimeTextStyle>>,

    @ColumnInfo("inline_spoilers")
    val inlineSpoilers: List<ProtoSeanimeTextRange<Unit>>,

    @ColumnInfo("spoiler_blocks")
    val spoilerBlocks: List<ProtoSeanimeTextRange<ProtoSeanimeTextSpoilerBlockItem>>,

    @ColumnInfo("links")
    val links: List<ProtoSeanimeTextRange<String>>,
)

fun SeanimeTextPOJO.asExternalModel() = SeanimeText(
    text = text,
    styles = styles.map { style ->
        style.asExternalModel { styleItem -> styleItem.asExternalModel() }
    }.toImmutableList(),
    inlineSpoilers = inlineSpoilers.map { spoiler ->
        spoiler.asExternalModel { }
    }.toImmutableList(),
    spoilerBlocks = spoilerBlocks.map { spoiler ->
        spoiler.asExternalModel { it.asExternalModel() }
    }.toImmutableList(),
    links = links.map { link -> link.asExternalModel { it } }.toImmutableList()
)