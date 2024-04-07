package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.MangaRepository
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.isMain
import javax.inject.Inject
import javax.inject.Provider

class GetEntryDetailsUseCase @Inject constructor(
    private val animeRepositoryProvider: Provider<AnimeRepository>,
    private val mangaRepositoryProvider: Provider<MangaRepository>
) {
    operator fun invoke(
        entryType: EntryType,
        entryId: Long
    ): Flow<EntryDetails> = when (entryType) {
        EntryType.Anime -> animeRepositoryProvider.get().let { animeRepository ->
            animeRepository.getEntryDetails(entryId)
                .map { details ->
                    val anime = requireNotNull(details.anime)
                    val sortedAnimeDetails = anime.copy(
                        characters = anime.characters?.sortedBy { !it.isMain },
                        authors = anime.authors?.sortedBy { !it.isMain() }
                    )
                    details.copy(anime = sortedAnimeDetails)
                }
        }

        else -> mangaRepositoryProvider.get().let { mangaRepository ->
            mangaRepository.getEntryDetails(entryId)
                .map { details ->
                    val manga = requireNotNull(details.manga)
                    val sortedMangaDetails = manga.copy(
                        characters = manga.characters?.sortedBy { !it.isMain },
                        authors = manga.authors?.sortedBy { !it.isMain() }
                    )
                    details.copy(manga = sortedMangaDetails)
                }
        }
    }
}