package ru.vladsaybulin.database.models.text

import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.annotatedtext.SeanimeText

@Serializable
sealed class ProtoSeanimeTextStyle {

    @Serializable
    data class ReadyStyle(val value: ProtoReadyStyleValue) : ProtoSeanimeTextStyle()

}

fun ProtoSeanimeTextStyle.asExternalModel(): SeanimeText.Style = when (this) {
    is ProtoSeanimeTextStyle.ReadyStyle -> SeanimeText.Style.ReadyStyle(value.asExternalModel())
}