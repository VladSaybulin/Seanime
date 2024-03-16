package ru.vladsaybulin.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.IO
import ru.vladsaybulin.data.repository.AnimeRepository
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.isMain
import javax.inject.Inject
import javax.inject.Provider

class GetEntryDetailsUseCase @Inject constructor(
    private val animeRepositoryProvider: Provider<AnimeRepository>,
    private val userRateRepository: UserRateRepository,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        entryType: EntryType,
        entryId: Long
    ): Flow<EntryDetails> = when (entryType) {
        EntryType.Anime -> animeRepositoryProvider.get().let { animeRepository ->
            combine(
                animeRepository.getAnimeDetails(entryId),
                animeRepository.getSimilarAnimes(entryId),
                userRateRepository.getAnimeUserRate(entryId)
            ) { details, similar, userRate ->
                val sortedAnimeDetails = details.copy(
                    characters = details.characters?.sortedBy { !it.isMain },
                    authors = details.authors?.sortedBy { !it.isMain() }
                )
                EntryDetails(
                    anime = sortedAnimeDetails,
                    userRate = userRate,
                    similarEntries = similar
                )
            }
        }
        else -> TODO()
    }
        .flowOn(ioDispatcher)
}