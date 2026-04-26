/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.data.repository

import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.manga.mangaKind
import ru.vladsaybulin.model.manga.ranobeKind
import ru.vladsaybulin.model.search.Duration
import ru.vladsaybulin.model.search.FilterOption
import ru.vladsaybulin.model.search.Filters
import ru.vladsaybulin.model.search.SeasonOfYear
import ru.vladsaybulin.model.search.TimePeriodAiring
import ru.vladsaybulin.model.userrate.UserRateStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiltersRepository @Inject constructor(
    private val filterGenreRepository: FilterGenreRepository,
    private val filterStudioRepository: FilterStudioRepository,
    private val filterPublisherRepository: FilterPublisherRepository
) {

    private var cachedAnimeFilters: Filters? = null

    private var cachedMangaFilters: Filters? = null

    private var cachedRanobeFilters: Filters? = null

    suspend fun getAnimeFilters() = if (cachedAnimeFilters == null) {
        Filters(
            animeKindOptions = animeKindOptions(),
            statusOptions = animeStatusOptions(),
            myListStatus = myListStatusOptions(),
            timePeriodAiringFilterOptions = seasonOptions(),
            duration = durationOptions(),
            ratingOptions = ratingOptions(),
            genresOption = animeGenresOptions(),
            themesOptions = animeThemesOptions(),
            demographicOptions = animeDemographicOptions(),
            studiosOptions = studioOptions()
        ).also { cachedAnimeFilters = it }
    } else cachedAnimeFilters!!

    suspend fun getMangaFilters() = if (cachedMangaFilters == null) {
        Filters(
            mangaKindOptions = mangaKindOptions(),
            statusOptions = mangaStatusOptions(),
            myListStatus = myListStatusOptions(),
            genresOption = mangaGenresOptions(),
            themesOptions = mangaThemesOptions(),
            demographicOptions = mangaDemographicOptions(),
            publishersOptions = publisherOptions()
        ).also { cachedMangaFilters = it }
    } else cachedMangaFilters!!

    suspend fun getRanobeFilters() = if (cachedRanobeFilters == null) {
        Filters(
            mangaKindOptions = ranobeKindOptions(),
            statusOptions = mangaStatusOptions(),
            myListStatus = myListStatusOptions(),
            genresOption = mangaGenresOptions(),
            themesOptions = mangaThemesOptions(),
            demographicOptions = mangaDemographicOptions(),
            publishersOptions = publisherOptions()
        )
    } else cachedRanobeFilters!!

    private fun animeKindOptions() = AnimeKind.entries
        .filter { it != AnimeKind.None }
        .map(AnimeKind::toOption)

    private fun mangaKindOptions() = mangaKind.map(MangaKind::toOption)

    private fun ranobeKindOptions() = ranobeKind.map(MangaKind::toOption)

    private fun animeStatusOptions() = listOf(
        EntryStatus.Anons,
        EntryStatus.Ongoing,
        EntryStatus.Released
    )
        .map(EntryStatus::toOption)

    private fun mangaStatusOptions() = EntryStatus.entries
        .filter { it != EntryStatus.None }
        .map(EntryStatus::toOption)

    private fun myListStatusOptions() = UserRateStatus.entries
        .filter { it != UserRateStatus.None }
        .map(UserRateStatus::toOption)

    private fun seasonOptions() = buildList {
        add(TimePeriodAiring.Season(SeasonOfYear.Summer, 2024).toOption())
        add(TimePeriodAiring.Season(SeasonOfYear.Spring, 2024).toOption())
        add(TimePeriodAiring.Season(SeasonOfYear.Winter, 2024).toOption())
        add(TimePeriodAiring.Season(SeasonOfYear.Fall, 2023).toOption())
        add(TimePeriodAiring.Year(2024).toOption())
        add(TimePeriodAiring.Year(2023).toOption())
        add(TimePeriodAiring.YearRange(2022, 2020).toOption())
        add(TimePeriodAiring.YearRange(2016, 2020).toOption())
        add(TimePeriodAiring.YearRange(2011, 2015).toOption())
        add(TimePeriodAiring.YearRange(2000, 2010).toOption())
        add(TimePeriodAiring.Decade(199).toOption())
        add(TimePeriodAiring.Decade(198).toOption())
    }

    private fun durationOptions() = Duration.entries.map(Duration::toOption)

    private fun ratingOptions() = AnimeRating.entries
        .filter { it != AnimeRating.None }
        .map(AnimeRating::toOption)

    private suspend fun animeGenresOptions() =
        filterGenreRepository.getGenres(EntryType.Anime, GenreKind.Genre)
            .map(Genre::toOption)

    private suspend fun animeThemesOptions() =
        filterGenreRepository.getGenres(EntryType.Anime, GenreKind.Theme)
            .map(Genre::toOption)

    private suspend fun animeDemographicOptions() =
        filterGenreRepository.getGenres(EntryType.Anime, GenreKind.Demographic)
            .map(Genre::toOption)

    private suspend fun mangaGenresOptions() =
        filterGenreRepository.getGenres(EntryType.Manga, GenreKind.Genre)
            .map(Genre::toOption)

    private suspend fun mangaThemesOptions() =
        filterGenreRepository.getGenres(EntryType.Manga, GenreKind.Theme)
            .map(Genre::toOption)

    private suspend fun mangaDemographicOptions() =
        filterGenreRepository.getGenres(EntryType.Manga, GenreKind.Demographic)
            .map(Genre::toOption)

    private suspend fun studioOptions() = filterStudioRepository.getFilterStudios()
        .map(Studio::toOption)

    private suspend fun publisherOptions() = filterPublisherRepository.getFilterPublishers()
        .map(Publisher::toOption)
}

private fun AnimeKind.toOption() = FilterOption(
    value = this,
    serializedValue = serializedName
)

private fun MangaKind.toOption() = FilterOption(
    value = this,
    serializedValue = serializedName
)

private fun EntryStatus.toOption() = FilterOption(
    value = this,
    serializedValue = serializedName
)

private fun UserRateStatus.toOption() = FilterOption(
    value = this,
    serializedValue = serializedName
)

fun TimePeriodAiring.toOption() = FilterOption(
    value = this,
    serializedValue = serializedValue
)

fun Duration.toOption() = FilterOption(
    value = this,
    serializedValue = serializedValue
)

fun AnimeRating.toOption() = FilterOption(
    value = this,
    serializedValue = serializedName
)

fun Genre.toOption() = FilterOption(
    value = this,
    serializedValue = id.toString()
)

fun Studio.toOption() = FilterOption(
    value = this,
    serializedValue = id.toString()
)

fun Publisher.toOption() = FilterOption(
    value = this,
    serializedValue = id.toString()
)