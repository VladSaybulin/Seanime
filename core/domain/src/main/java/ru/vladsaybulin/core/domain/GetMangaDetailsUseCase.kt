package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.combine
import ru.vladsaybulin.data.repository.MangaRepository
import javax.inject.Inject

class GetMangaDetailsUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
    operator fun invoke(animeId: Long) = combine(
        mangaRepository.getMangaDetails(animeId),
        mangaRepository.getMainMangaAuthors(animeId),
        mangaRepository.getMainMangaCharacters(animeId)
    ) { details, authors, characters ->
        details.copy(
            authors = authors,
            characters = characters
        )
    }
}