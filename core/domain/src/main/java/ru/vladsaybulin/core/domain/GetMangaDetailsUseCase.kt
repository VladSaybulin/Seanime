package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.repository.MangaRepository
import javax.inject.Inject

class GetMangaDetailsUseCase @Inject constructor(
    private val mangaRepository: MangaRepository
) {
    operator fun invoke(animeId: Long) = mangaRepository.getMangaDetails(animeId)
        .map {
            it.copy(
                characters = it.characters?.sortedBy { !it.isMain },
                authors = it.authors?.sortedBy { personWithRoles -> !personWithRoles.isMain },
            )
        }
}