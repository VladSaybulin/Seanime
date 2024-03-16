package ru.vladsaybulin.feature.details.model

import kotlinx.datetime.Instant
import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.EntryType
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.IncompleteDate
import ru.vladsaybulin.model.Studio
import ru.vladsaybulin.model.isNullOrEmpty

sealed interface DetailsInfo {
    data class StatusDates(
        val entryType: EntryType,
        val status: EntryStatus,
        val airedOn: IncompleteDate?,
        val releasedOn: IncompleteDate?,
        override val key: String = "status"
    ) : DetailsInfo {
        override val shouldShow: Boolean =
            status != EntryStatus.None
                    || airedOn.isNullOrEmpty()
                    || releasedOn.isNullOrEmpty()
    }

    data class AnimeKindEpisodes(
        val kind: AnimeKind,
        val episodes: Int,
        val episodesAired: Int,
        val duration: Int?,
        val ongoing: Boolean,
        override val key: String = "anime_kind"
    ) : DetailsInfo {
        override val shouldShow: Boolean =
            kind != AnimeKind.None
                    || episodes > 1
                    || duration != null
    }

    data class NextEpisode(
        val nextEpisodeDate: Instant,
        override val key: String = "next_episode"
    ) : DetailsInfo {
        override val shouldShow: Boolean = true
    }

    data class Studios(
        val studios: List<Studio>,
        override val key: String = "studios"
    ) : DetailsInfo {
        override val shouldShow: Boolean = studios.isNotEmpty()
    }

    data class Genres(
        val genres: List<Genre>,
        val headerStringId: Int,
        override val key: String
    ) : DetailsInfo {
        override val shouldShow: Boolean = !genres.isNullOrEmpty()
    }

    val shouldShow: Boolean

    //For lazy list keys
    val key: String
}

