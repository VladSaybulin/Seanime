package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailsUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    operator fun invoke(animeId: Long) = animeRepository.getAnimeDetails(animeId)
        .map {
            it.copy(
                characters = it.characters?.sortedBy { !it.isMain },
                authors = it.authors?.sortedBy { personWithRoles -> !personWithRoles.isMain },
            )
        }
}