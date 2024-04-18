package ru.vladsaybulin.data.repository

import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.genre.Genre
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.manga.Publisher
import ru.vladsaybulin.model.anime.Studio
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.search.Duration
import ru.vladsaybulin.model.search.FilterOption
import ru.vladsaybulin.model.search.Filters
import ru.vladsaybulin.model.search.Season
import ru.vladsaybulin.model.search.SeasonFilter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiltersRepository @Inject constructor(
    private val genreRepository: GenreRepository,
    private val studioRepository: StudioRepository,
    private val publisherRepository: PublisherRepository
) {

    private var cachedAnimeFilters: Filters? = null

    private var cachedMangaFilters: Filters? = null

    suspend fun getAnimeFilters() = if (cachedAnimeFilters == null) {
        Filters(
            animeKindOptions = animeKindOptions(),
            statusOptions = animeStatusOptions(),
            myListStatus = myListStatusOptions(),
            seasonFilterOptions = seasonOptions(),
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

    private fun animeKindOptions() = AnimeKind.entries
        .filter { it != AnimeKind.None }
        .map(AnimeKind::toOption)

    private fun mangaKindOptions() = MangaKind.entries
        .filter { it != MangaKind.None }
        .map(MangaKind::toOption)

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
        add(SeasonFilter.SeasonYear(Season.Summer, 2024).toOption())
        add(SeasonFilter.SeasonYear(Season.Spring, 2024).toOption())
        add(SeasonFilter.SeasonYear(Season.Winter, 2024).toOption())
        add(SeasonFilter.SeasonYear(Season.Fall, 2023).toOption())
        add(SeasonFilter.Year(2024).toOption())
        add(SeasonFilter.Year(2023).toOption())
        add(SeasonFilter.YearRange(2022, 2020).toOption())
        add(SeasonFilter.YearRange(2016, 2020).toOption())
        add(SeasonFilter.YearRange(2011, 2015).toOption())
        add(SeasonFilter.YearRange(2000, 2010).toOption())
        add(SeasonFilter.Decade(199).toOption())
        add(SeasonFilter.Decade(198).toOption())
    }

    private fun durationOptions() = Duration.entries.map(Duration::toOption)

    private fun ratingOptions() = AnimeRating.entries
        .filter { it != AnimeRating.None }
        .map(AnimeRating::toOption)

    private suspend fun animeGenresOptions() =
        genreRepository.getGenres(EntryType.Anime, GenreKind.Genre)
            .map(Genre::toOption)

    private suspend fun animeThemesOptions() =
        genreRepository.getGenres(EntryType.Anime, GenreKind.Theme)
            .map(Genre::toOption)

    private suspend fun animeDemographicOptions() =
        genreRepository.getGenres(EntryType.Anime, GenreKind.Demographic)
            .map(Genre::toOption)

    private suspend fun mangaGenresOptions() =
        genreRepository.getGenres(EntryType.Manga, GenreKind.Genre)
            .map(Genre::toOption)

    private suspend fun mangaThemesOptions() =
        genreRepository.getGenres(EntryType.Manga, GenreKind.Theme)
            .map(Genre::toOption)

    private suspend fun mangaDemographicOptions() =
        genreRepository.getGenres(EntryType.Manga, GenreKind.Demographic)
            .map(Genre::toOption)

    private suspend fun studioOptions() = studioRepository.getStudios()
        .map(Studio::toOption)

    private suspend fun publisherOptions() = publisherRepository.getPublishers()
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

fun SeasonFilter.toOption() = FilterOption(
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