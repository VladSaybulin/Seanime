package ru.vladsaybulin.feature.userrate

import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType

data class UserRateEditorContext(
    val entryType: EntryType,
    val entryStatus: EntryStatus,
    val episodesLimit: Limit? = null,
    val chaptersLimit: Limit? = null,
    val volumesLimit: Limit? = null,
)

sealed class Limit(val limit: Int) {

    data object Unlimited : Limit(Int.MAX_VALUE)

    class Limited(limit: Int) : Limit(limit)

    val range = 0..limit
}

//fun Anime.toUserRateEditorContext() = UserRateEditorContext(
//    entryType = EntryType.Anime,
//    entryStatus = status,
//    episodesLimit = when {
//        episodes == 1 -> null // Нет необходимости в счётчике
//        status == Ongoing && episodesAired > 0 -> Limit.Limited(episodesAired)
//        episodes > 0 -> Limit.Limited(episodes)
//        else -> Limit.Unlimited
//    },
//    chaptersLimit = null,
//    volumesLimit = null,
//)