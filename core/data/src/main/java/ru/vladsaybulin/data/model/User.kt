package ru.vladsaybulin.data.model

import ru.vladsaybulin.database.models.user.UserEntity
import ru.vladsaybulin.database.models.user.UserImagePOJO
import ru.vladsaybulin.network.models.NetworkBriefUser
import ru.vladsaybulin.network.models.UserImageDto

fun NetworkBriefUser.asExternalModel() = UserEntity(
    id = id,
    nickname = nickname,
    avatarUrl = avatarUrl,
    image = image.asPOJO(),
    lastOnlineAt = lastOnlineAt,
    url = url
)

private fun UserImageDto.asPOJO() = UserImagePOJO(
    x160Url = x160Url,
    x148Url = x148Url,
    x80Url = x80Url,
    x64Url = x64Url,
    x48Url = x48Url,
    x32Url = x32Url,
    x16Url = x16Url
)