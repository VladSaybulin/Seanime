package ru.vladsaybulin.core.navigation

import ru.vladsaybulin.model.common.EntryType

sealed class NavigationEvent {

    data object Back : NavigationEvent()

    data class EntryDetails(val entryType: EntryType, val entryId: Long) : NavigationEvent()

    data class CharacterDetails(val characterId: Long) : NavigationEvent()

}
