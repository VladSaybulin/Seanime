package ru.vladsaybulin.core.domain

import ru.vladsaybulin.model.character.CharacterWithRole
import javax.inject.Inject

class SearchCharactersUseCase @Inject constructor() {
    operator fun invoke(
        characters: List<CharacterWithRole>,
        searchQuery: String
    ): List<CharacterWithRole> {
        if (searchQuery.isBlank()) return characters

        return characters.filter { characterWithRole ->
            characterWithRole.character.run {
                originalName.contains(searchQuery, ignoreCase = true) ||
                        (russianName != null &&
                                russianName!!.contains(searchQuery, ignoreCase = true))
            }
        }
    }
}
