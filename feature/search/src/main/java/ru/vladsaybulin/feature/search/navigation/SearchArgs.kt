package ru.vladsaybulin.feature.search.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.genre.GenreKind
import ru.vladsaybulin.model.search.SearchType
import kotlin.reflect.typeOf

@Serializable
data class SearchArgs internal constructor(
    val searchType: SearchType,
    val entryStatus: EntryStatus = EntryStatus.None,
    val genreKind: GenreKind = GenreKind.None,
    val genreId: Long = UNSPECIFIED_ID,
    val studioId: Long = UNSPECIFIED_ID,
    val publisherId: Long = UNSPECIFIED_ID
) {
    companion object {

        fun searchByGenreArgs(
            searchType: SearchType,
            genreKind: GenreKind,
            genreId: Long
        ) = SearchArgs(
            searchType = searchType,
            genreKind = genreKind,
            genreId = genreId
        )

        fun searchAnimeOngoing() = SearchArgs(
            searchType = SearchType.Anime,
            entryStatus = EntryStatus.Ongoing
        )

        fun searchAnimeByStudio(studioId: Long) = SearchArgs(
            searchType = SearchType.Anime,
            studioId = studioId
        )

        fun searchMangaOrRanobeByPublisher(
            isManga: Boolean,
            publisherId: Long
        ) = SearchArgs(
            searchType = if (isManga) SearchType.Manga else SearchType.Ranobe,
            publisherId = publisherId
        )

        fun defaultSearch() = SearchArgs(SearchType.Anime)
    }
}

val SearchArgsNavType = mapOf(
    typeOf<SearchType>() to NavType.EnumType(SearchType::class.java),
    typeOf<GenreKind>() to NavType.EnumType(GenreKind::class.java),
    typeOf<EntryStatus>() to NavType.EnumType(EntryStatus::class.java)
)

internal fun SavedStateHandle.toSearchArgs() = toRoute<SearchArgs>(SearchArgsNavType)

internal const val UNSPECIFIED_ID = -1L
