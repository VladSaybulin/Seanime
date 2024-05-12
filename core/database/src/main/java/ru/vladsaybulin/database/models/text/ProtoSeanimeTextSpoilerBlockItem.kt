package ru.vladsaybulin.database.models.text

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.annotatedtext.SeanimeText

@Serializable
enum class ProtoSeanimeTextSpoilerBlockItem {
    Block, Title
}

fun ProtoSeanimeTextSpoilerBlockItem.asExternalModel() = when (this) {
    ProtoSeanimeTextSpoilerBlockItem.Block -> SeanimeText.SpoilerBlockItem.Block
    ProtoSeanimeTextSpoilerBlockItem.Title -> SeanimeText.SpoilerBlockItem.Title
}