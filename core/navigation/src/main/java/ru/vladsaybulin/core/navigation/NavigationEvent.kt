package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.userrate.UserRateWithEntry

sealed class NavigationEvent {

    data object Back : NavigationEvent()

    data class EntryDetails(val entryType: EntryType, val entryId: Long) : NavigationEvent()

    data class CharacterDetails(val characterId: Long) : NavigationEvent()

    data class PersonDetails(val personId: Long) : NavigationEvent()

    data class SearchByGenre(val entryType: EntryType, val genre: Long) : NavigationEvent()

    data class SearchByStudioOrPublisher(
        val entryType: EntryType,
        val studioOrPublisher: Long
    ) : NavigationEvent()

    data object OngoingAnimes : NavigationEvent()

    data class UserRate(val userRateWithEntry: UserRateWithEntry) : NavigationEvent()

    data class ImageView(val images: List<Image>, val imageIndex: Int) : NavigationEvent()

    data object RequireAuth : NavigationEvent()

}
