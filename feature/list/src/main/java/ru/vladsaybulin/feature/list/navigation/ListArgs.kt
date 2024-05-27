package ru.vladsaybulin.feature.list.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRateStatus
import kotlin.reflect.typeOf

@Serializable
class ListArgs(
    val userId: Long = MY_ID,
    val titleType: EntryType = EntryType.Anime,
    val status: UserRateStatus = UserRateStatus.Watching,
) {
    companion object {
        const val MY_ID = -1L
    }
}

val ListArgsNavType = mapOf(
    typeOf<EntryType>() to NavType.EnumType(EntryType::class.java),
    typeOf<UserRateStatus>() to NavType.EnumType(UserRateStatus::class.java)
)

internal fun SavedStateHandle.toListArgs() = toRoute<ListArgs>(ListArgsNavType)