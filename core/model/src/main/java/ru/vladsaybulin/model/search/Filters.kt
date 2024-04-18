package ru.vladsaybulin.model.search

import ru.vladsaybulin.model.AnimeKind
import ru.vladsaybulin.model.AnimeRating
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.Genre
import ru.vladsaybulin.model.MangaKind
import ru.vladsaybulin.model.Publisher
import ru.vladsaybulin.model.Studio
import ru.vladsaybulin.model.UserRateStatus

data class Filters(
    val animeKindOptions: List<FilterOption<AnimeKind>>? = null,
    val mangaKindOptions: List<FilterOption<MangaKind>>? = null,
    val statusOptions: List<FilterOption<EntryStatus>>? = null,
    val seasonFilterOptions: List<FilterOption<SeasonFilter>>? = null,
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

