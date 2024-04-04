package ru.vladsaybulin.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.isMain
import javax.inject.Inject
import javax.inject.Provider

class GetEntryDetailsUseCase @Inject constructor(private val animeRepositoryProvider: Provider<AnimeRepository>) {
    operator fun invoke(
        entryType: EntryType,
        entryId: Long
    ): Flow<EntryDetails> = when (entryType) {
        EntryType.Anime -> animeRepositoryProvider.get().let { animeRepository ->
            combine(
                animeRepository.getAnimeDetails(entryId),
                animeRepository.getSimilarAnimes(entryId),
            ) { details, similar->
                val sortedAnimeDetails = details.copy(
                    characters = details.characters?.sortedBy { !it.isMain },
                    authors = details.authors?.sortedBy { !it.isMain() }
                )
                EntryDetails(
                    anime = sortedAnimeDetails,
                    similarEntries = similar
                )
            }
        }
        else -> TODO()
    }
}