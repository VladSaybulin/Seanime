package ru.vladsaybulin.model.search

import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.userrate.UserRateStatus

data class Filters(
    val animeKindOptions: List<FilterOption<AnimeKind>>? = null,
    val mangaKindOptions: List<FilterOption<MangaKind>>? = null,
    val statusOptions: List<FilterOption<EntryStatus>>? = null,
    val timePeriodAiringFilterOptions: List<FilterOption<TimePeriodAiring>>? = null,
    val myListStatus: List<FilterOption<UserRateStatus>>? = null,
    val duration: List<FilterOption<Duration>>? = null,
    val ratingOptions: List<FilterOption<AnimeRating>>? = null,
    val score: List<FilterOption<Int>>? = null,
    val genresOption: List<FilterOption<Genre>>? = null,
    val themesOptions: List<FilterOption<Genre>>? = null,
    val demographicOptions: List<FilterOption<Genre>>? = null,
    val studiosOptions: List<FilterOption<Studio>>? = null,
    val publishersOptions: List<FilterOption<Publisher>>? = null
)

