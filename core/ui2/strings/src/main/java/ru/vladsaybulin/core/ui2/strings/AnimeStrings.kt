package ru.vladsaybulin.core.ui2.strings

import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.anime.AnimeRating
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.userrate.UserRateStatus

object AnimeStrings : DependsOnTitleStrings {
    override fun titleStatusId(status: EntryStatus): Int = when(status) {
        EntryStatus.Anons -> R.string.core_ui2_strings_anime_status_anons
        EntryStatus.Ongoing -> R.string.core_ui2_strings_anime_status_ongoing
        EntryStatus.Released -> R.string.core_ui2_strings_anime_status_released
        EntryStatus.Paused -> R.string.core_ui2_strings_anime_status_paused
        EntryStatus.Discontinued -> R.string.core_ui2_strings_anime_status_discontinued
        EntryStatus.None -> R.string.core_ui2_strings_none
    }

    override fun userStatusId(status: UserRateStatus): Int = when (status) {
        UserRateStatus.Planned -> R.string.core_ui2_strings_anime_user_status_status_planned
        UserRateStatus.Watching -> R.string.core_ui2_strings_anime_user_status_watching
        UserRateStatus.Rewatching -> R.string.core_ui2_strings_anime_user_status_rewatching
        UserRateStatus.Completed -> R.string.core_ui2_strings_anime_user_status_completed
        UserRateStatus.OnHold -> R.string.core_ui2_strings_anime_user_status_on_hold
        UserRateStatus.Dropped -> R.string.core_ui2_strings_anime_user_status_dropped
        UserRateStatus.None -> R.string.core_ui2_strings_none
    }

    fun kindId(kind: AnimeKind): Int = when(kind) {
        AnimeKind.Tv -> R.string.core_ui2_strings_anime_kind_tv
        AnimeKind.Movie -> R.string.core_ui2_strings_anime_kind_movie
        AnimeKind.Ona -> R.string.core_ui2_strings_anime_kind_ona
        AnimeKind.Ova -> R.string.core_ui2_strings_anime_kind_ova
        AnimeKind.Music -> R.string.core_ui2_strings_anime_kind_music
        AnimeKind.Special -> R.string.core_ui2_strings_anime_kind_special
        AnimeKind.TvSpecial -> R.string.core_ui2_strings_anime_kind_tv_special
        AnimeKind.Pv -> R.string.core_ui2_strings_anime_kind_pv
        AnimeKind.Cm -> R.string.core_ui2_strings_anime_kind_cm
        AnimeKind.None -> R.string.core_ui2_strings_none
    }

    fun ratingId(rating: AnimeRating): Int = when (rating) {
        AnimeRating.G -> R.string.core_ui2_strings_anime_rating_g
        AnimeRating.PG -> R.string.core_ui2_strings_anime_rating_pg
        AnimeRating.PG13 -> R.string.core_ui2_strings_anime_rating_pg13
        AnimeRating.R -> R.string.core_ui2_strings_anime_rating_r
        AnimeRating.RPlus -> R.string.core_ui2_strings_anime_rating_r_plus
        AnimeRating.RX -> R.string.core_ui2_strings_anime_rating_rx
        AnimeRating.None -> R.string.core_ui2_strings_none
    }

    sealed class ProgressFormat {
        data class TotalOnly(val total: Int) : ProgressFormat()
        data class AiredOfUnknown(val aired: Int) : ProgressFormat()
        data class AiredOfTotal(val aired: Int, val total: Int) : ProgressFormat()
    }

    fun getProgressFormat(aired: Int, total: Int, isOngoing: Boolean, isMovie: Boolean): ProgressFormat? {
        if (isMovie && total <= 1) return null
        return if (total > 1 || (total == 0 && aired > 0)) {
            when {
                isOngoing && total > 0 -> ProgressFormat.AiredOfTotal(aired, total)
                isOngoing -> ProgressFormat.AiredOfUnknown(aired)
                else -> ProgressFormat.TotalOnly(total)
            }
        } else null
    }
}

