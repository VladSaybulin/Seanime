package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import ru.vladsaybulin.data.repository.AnimeRepository
import javax.inject.Inject

class GetAnimeDetailsUseCase @Inject constructor(
    private val animeRepository: AnimeRepository
) {
    operator fun invoke(animeId: Long) =
        combine(
            animeRepository.getAnimeDetails(animeId),
            animeRepository.getMainAnimeAuthors(animeId),
            animeRepository.getMainAnimeCharacters(animeId)
        ) { details, authors, characters ->
            details.copy(
                authors = authors,
                characters = characters
            )
        }
            .onStart { animeRepository.syncAnimeDetails(animeId) }
}