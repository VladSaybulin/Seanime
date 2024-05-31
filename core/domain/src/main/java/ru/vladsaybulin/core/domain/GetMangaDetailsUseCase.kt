package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import ru.vladsaybulin.data.repository.MangaRepository
import javax.inject.Inject

class GetMangaDetailsUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
    operator fun invoke(mangaId: Long) = combine(
        mangaRepository.getMangaDetails(mangaId),
        mangaRepository.getMainMangaAuthors(mangaId),
        mangaRepository.getMainMangaCharacters(mangaId)
    ) { details, authors, characters ->
        details.copy(
            authors = authors,
            characters = characters
        )
    }
        .onStart { mangaRepository.syncMangaDetails(mangaId) }
}