package ru.vladsaybulin.core.domain

import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.person.PersonWithRoles
import javax.inject.Inject

class GetAuthorsUseCase @Inject constructor(
    private val animeRepository: Lazy<AnimeRepository>,
    private val mangaRepository: Lazy<MangaRepository>
) {
    operator fun invoke(entryType: EntryType, entryId: Long): Flow<List<PersonWithRoles>> =
        when (entryType) {
            EntryType.Anime -> animeRepository.get().getAllAnimeAuthors(entryId)
            EntryType.Manga -> mangaRepository.get().getAllMangaAuthors(entryId)
        }
}