package ru.vladsaybulin.database.models.user

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import ru.vladsaybulin.model.user.BriefUser

@Entity(tableName = "users")
data class UserEntity(

    @ColumnInfo("id")
    @PrimaryKey
    val id: Long,

    @ColumnInfo("nickname")
    val nickname: String,

    @ColumnInfo("avatar")
    val avatarUrl: String,

    @Embedded("image_")
    val image: UserImagePOJO,

    @ColumnInfo("last_online_at")
    val lastOnlineAt: Instant,

    @ColumnInfo("url")
    val url: String
)

fun UserEntity.asExternalModel() = BriefUser(
    id = id,
    nickname = nickname,
    avatarUrl = avatarUrl,
    image = image.asExternalModel(),
    lastOnlineAt = lastOnlineAt,
    url = url
)