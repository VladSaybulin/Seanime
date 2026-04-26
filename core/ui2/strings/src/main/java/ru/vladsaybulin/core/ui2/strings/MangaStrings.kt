package ru.vladsaybulin.core.ui2.strings

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.userrate.UserRateStatus

object MangaStrings : DependsOnTitleStrings {
    override val titleType: EntryType = EntryType.Manga

    override fun titleStatusId(status: EntryStatus): Int = when (status) {
        EntryStatus.Anons -> R.string.core_ui2_strings_manga_status_anons
        EntryStatus.Ongoing -> R.string.core_ui2_strings_manga_status_ongoing
        EntryStatus.Released -> R.string.core_ui2_strings_manga_status_released
        EntryStatus.Paused -> R.string.core_ui2_strings_manga_status_paused
        EntryStatus.Discontinued -> R.string.core_ui2_strings_manga_status_discontinued
        EntryStatus.None -> R.string.core_ui2_strings_none
    }

    override fun userStatusId(status: UserRateStatus): Int = when (status) {
        UserRateStatus.Planned -> R.string.core_ui2_strings_manga_user_status_planned
        UserRateStatus.Watching -> R.string.core_ui2_strings_manga_user_status_watching
        UserRateStatus.Rewatching -> R.string.core_ui2_strings_manga_user_status_rewatching
        UserRateStatus.Completed -> R.string.core_ui2_strings_manga_user_status_completed
        UserRateStatus.OnHold -> R.string.core_ui2_strings_manga_user_status_on_hold
        UserRateStatus.Dropped -> R.string.core_ui2_strings_manga_user_status_dropped
        UserRateStatus.None -> R.string.core_ui2_strings_none
    }

    fun kindId(kind: MangaKind): Int = when (kind) {
        MangaKind.Manga -> R.string.core_ui2_strings_manga_kind_manga
        MangaKind.Manhwa -> R.string.core_ui2_strings_manga_kind_manhwa
        MangaKind.Manhua -> R.string.core_ui2_strings_manga_kind_manhua
        MangaKind.OneShot -> R.string.core_ui2_strings_manga_kind_oneshot
        MangaKind.Doujin -> R.string.core_ui2_strings_manga_kind_doujin
        MangaKind.LightNovel -> R.string.core_ui2_strings_manga_kind_light_novel
        MangaKind.Novel -> R.string.core_ui2_strings_manga_kind_novel
        MangaKind.None -> R.string.core_ui2_strings_none
    }
}