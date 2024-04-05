package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.EntryStatus
import ru.vladsaybulin.model.MangaKind
import ru.vladsaybulin.model.UserRateStatus

@Composable
@ReadOnlyComposable
fun chaptersAndVolumesString(
    volumes: Int,
    chapters: Int
): String? {
    val volumesText = if (volumes > 0) {
        pluralStringResource(id = R.plurals.volumes, count = volumes, volumes)
    } else null

    val chaptersText = if (chapters > 0) {
        pluralStringResource(id = R.plurals.chapters, count = chapters, chapters)
    } else null

    if (volumesText == null && chaptersText == null)
        return null

    return buildString {
        volumesText?.let { append(it) }
        chaptersText?.let {
            if (isNotEmpty()) append(", ")
            append(it)
        }
    }
}

fun mangaKindStringResId(mangaKind: MangaKind) = when (mangaKind) {
    MangaKind.Manga -> R.string.manga_kind_manga
    MangaKind.Manhwa -> R.string.manga_kind_manhwa
    MangaKind.Manhua -> R.string.manga_kind_manhua
    MangaKind.OneShot -> R.string.manga_kind_oneshot
    MangaKind.Doujin -> R.string.manga_kind_doujin
    MangaKind.LightNovel -> R.string.manga_kind_light_novel
    MangaKind.Novel -> R.string.manga_kind_novel
    MangaKind.None -> null
}

@Composable
@ReadOnlyComposable
fun mangaKindString(mangaKind: MangaKind) =
    mangaKindStringResId(mangaKind)?.let { stringResource(id = it) }


fun mangaUserRateStatusStringResId(userRateStatus: UserRateStatus) = when (userRateStatus) {
    UserRateStatus.Planned -> R.string.user_rate_status_planned
    UserRateStatus.Watching -> R.string.user_rate_status_reading
    UserRateStatus.Rewatching -> R.string.user_rate_status_rereading
    UserRateStatus.Completed -> R.string.user_rate_status_read
    UserRateStatus.OnHold -> R.string.user_rate_status_on_hold
    UserRateStatus.Dropped -> R.string.user_rate_status_dropped
    UserRateStatus.None -> null
}

@Composable
@ReadOnlyComposable
fun mangaUserRateStatusString(userRateStatus: UserRateStatus) =
    mangaUserRateStatusStringResId(userRateStatus)?.let { stringResource(id = it) }

@Composable
@ReadOnlyComposable
fun notNoneMangaUserRateStatusString(userRateStatus: UserRateStatus) =
    stringResource(
        id = requireNotNull(mangaUserRateStatusStringResId(userRateStatus)) {
            "UserRateStatus is None"
        }
    )

fun mangaStatusStringResId(status: EntryStatus) = when (status) {
    EntryStatus.Anons -> R.string.manga_status_anons
    EntryStatus.Ongoing -> R.string.manga_status_ongoing
    EntryStatus.Released -> R.string.manga_status_released
    EntryStatus.Paused -> R.string.manga_status_paused
    EntryStatus.Discontinued -> R.string.manga_status_discontinued
    EntryStatus.None -> null
}

@Composable
@ReadOnlyComposable
fun mangaStatusString(status: EntryStatus) =
    mangaStatusStringResId(status)?.let { stringResource(id = it) }