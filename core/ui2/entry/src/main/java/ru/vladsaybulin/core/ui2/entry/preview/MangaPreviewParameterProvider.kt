package ru.vladsaybulin.core.ui2.entry.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind

class MangaItemPreviewParameterProvider : PreviewParameterProvider<Manga> {
    override val values: Sequence<Manga> = mangas.asSequence()
}

class ListOfMangaPreviewParameterProvider : PreviewParameterProvider<List<Manga>> {
    override val values: Sequence<List<Manga>> = sequenceOf(mangas)
}

private val mangas = listOf(
    Manga(
        id = 35513,
        name = "Koten-bu Series",
        russianName = "Цикл историй литературного клуба",
        poster = Image("", ""),
        kind = MangaKind.Novel,
        status = EntryStatus.None,
        score = 8.61f,
        chapters = 0,
        volumes = 0,
        airedOn = IncompleteDate(day = 31, month = 10, year = 2001),
        releasedOn = null,
    ),
    Manga(
        id = 96792,
        name = "Kimetsu no Yaiba",
        russianName = "Клинок, рассекающий демонов",
        poster = Image("", ""),
        kind = MangaKind.None,
        status = EntryStatus.Released,
        score = 8.11f,
        chapters = 207,
        volumes = 23,
        airedOn = IncompleteDate(day = 15, month = 2, year = 2016),
        releasedOn = IncompleteDate(day = 18, month = 5, year = 2020),
    ),
    Manga(
        id = 163116,
        name = "The Stellar Swordmaster",
        russianName = "Мастер меча, охватывающий звёзды",
        poster = Image("", ""),
        kind = MangaKind.Manhwa,
        status = EntryStatus.Ongoing,
        score = 8.08f,
        chapters = 0,
        volumes = 0,
        airedOn = IncompleteDate(day = 19, month = 9, year = 2023),
        releasedOn = null,
    ),
    Manga(
        id = 35573,
        name = "Orange",
        russianName = "Орендж",
        poster = null,
        kind = MangaKind.Manga,
        status = EntryStatus.Released,
        score = 8.28f,
        chapters = 38,
        volumes = 0,
        airedOn = IncompleteDate(day = 13, month = 3, year = 2012),
        releasedOn = IncompleteDate(day = 8, month = 4, year = 2022),
    ),
    Manga(
        id = 1469,
        name = "Glass no Kamen",
        russianName = "Стеклянная маска",
        poster = Image("", ""),
        kind = MangaKind.Manhwa,
        status = EntryStatus.Paused,
        score = 8.42f,
        chapters = 0,
        volumes = 49,
        airedOn = IncompleteDate(day = null, month = 1, year = 1976),
        releasedOn = IncompleteDate(day = 26, month = 5, year = 2012),
    ),
)
