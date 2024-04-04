package ru.vladsaybulin.core.domain

import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.Anime
import ru.vladsaybulin.model.AnimeDetails
import ru.vladsaybulin.model.EntryDetails
import ru.vladsaybulin.model.Manga
import ru.vladsaybulin.model.MangaDetails
import ru.vladsaybulin.model.UserRateStatus
import ru.vladsaybulin.model.UserRateValues
import javax.inject.Inject

class CreateUserRateUseCase @Inject constructor(
    private val userRateRepository: UserRateRepository
) {
    suspend operator fun invoke(userRateStatus: UserRateStatus, entryDetails: EntryDetails) {
        if (entryDetails.anime != null) {
            userRateRepository.createUserRate(
                anime = entryDetails.anime!!.asBrief(),
                userRateValues = UserRateValues(
                    status = userRateStatus,
                    score = 0,
                    chapters = 0,
                    episodes = 0,
                    volumes = 0,
                    rewatches = 0,
                    text = ""
                )
            )
        } else {
            userRateRepository.createUserRate(
                manga = entryDetails.manga!!.asBrief(),
                userRateValues = UserRateValues(
                    status = userRateStatus,
                    score = 0,
                    chapters = 0,
                    episodes = 0,
                    volumes = 0,
                    rewatches = 0,
                    text = ""
                )
            )
        }
    }
}

private fun AnimeDetails.asBrief() = Anime(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster,
    kind = kind,
    status = status,
    score = score,
    episodes = episodes,
    episodesAired = episodesAired,
    airedOn = airedOn,
    releasedOn = releasedOn
)

private fun MangaDetails.asBrief() = Manga(
    id = id,
    originalName = originalName,
    russianName = russianName,
    poster = poster,
    kind = kind,
    status = status,
    score = score,
    chapters = chapters,
    volumes = volumes,
    airedOn = airedOn,
    releasedOn = releasedOn
)