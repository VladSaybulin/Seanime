package ru.vladsaybulin.feature.character.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetailsArgs(val characterId: Long)

fun SavedStateHandle.toCharacterDetailsArgs() = toRoute<CharacterDetailsArgs>()