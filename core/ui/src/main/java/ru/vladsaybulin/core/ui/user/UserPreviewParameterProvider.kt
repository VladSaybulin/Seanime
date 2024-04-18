package ru.vladsaybulin.core.ui.user

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.user.UserImage

class UserPreviewParameterProvider : PreviewParameterProvider<BriefUser> {
    override val values: Sequence<BriefUser> = previewBriefUsers.asSequence()
}

val previewBriefUsers = listOf(
    BriefUser(
        id = 1361053,
        nickname = "Siedlitzer",
        avatarUrl = "https://desu.shikimori.one/system/users/x48/1361053.png?1712514264",
        image = UserImage(
            x160Url = "https://desu.shikimori.one/system/users/x160/1361053.png?1712514264",
            x148Url = "https://desu.shikimori.one/system/users/x148/1361053.png?1712514264",
            x80Url = "https://desu.shikimori.one/system/users/x80/1361053.png?1712514264",
            x64Url = "https://desu.shikimori.one/system/users/x64/1361053.png?1712514264",
            x48Url = "https://desu.shikimori.one/system/users/x48/1361053.png?1712514264",
            x32Url = "https://desu.shikimori.one/system/users/x32/1361053.png?1712514264",
            x16Url = "https://desu.shikimori.one/system/users/x16/1361053.png?1712514264"
        ),
        lastOnlineAt = LocalDateTime(//"2024-04-08T23:33:56.711+03:00"
            year = 2024,
            monthNumber = 4,
            dayOfMonth = 8,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        ).toInstant(TimeZone.of("GMT+03:00")),
        url = "https://shikimori.one/Siedlitzer"
    )
)