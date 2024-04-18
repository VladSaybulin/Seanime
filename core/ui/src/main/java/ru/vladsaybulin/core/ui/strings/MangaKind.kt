package ru.vladsaybulin.core.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import ru.vladsaybulin.core.ui.R
import ru.vladsaybulin.model.manga.MangaKind

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