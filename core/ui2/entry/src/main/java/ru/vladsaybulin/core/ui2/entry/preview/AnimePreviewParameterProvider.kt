package ru.vladsaybulin.core.ui2.entry.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate

class AnimeItemPreviewParameterProvider : PreviewParameterProvider<Anime> {
    override val values: Sequence<Anime> = animes.asSequence()
    override val count: Int = animes.size
}

class ListOfAnimesPreviewParameterProvider : PreviewParameterProvider<List<Anime>> {
    override val values: Sequence<List<Anime>> = sequenceOf(animes)
}

private val animes = listOf(
    Anime(
        id = 17201,
        name = "Toki no Daichi: Hana no Oukoku no Majo",
        russianName = "Земля времени: Ведьма королевства цветов",
        poster = Image("", ""),
        kind = AnimeKind.Ova,
        score = 5.8f,
        status = EntryStatus.Released,
        episodes = 3,
        episodesAired = 0,
        airedOn = IncompleteDate(22, 12, 1998),
        releasedOn = IncompleteDate(25, 3, 1999),
        userRate = null
    ),
    Anime(
        id = 35575,
        name = "Nono-chan Theater",
        russianName = "Театр Ноно",
        poster = null,
        kind = AnimeKind.Ona,
        score = 0f,
        status = EntryStatus.None,
        episodes = 85,
        episodesAired = 0,
        airedOn = IncompleteDate(3, 9, 2001),
        releasedOn = IncompleteDate(28, 12, 2001),
        userRate = null
    ),
    Anime(
        id = 42603,
        name = "Boku no Hero Academia: Ikinokore! Kesshi no Survival Kunren",
        russianName = null,
        poster = Image("", ""),
        kind = AnimeKind.None,
        score = 7.16f,
        status = EntryStatus.Ongoing,
        episodes = 1,
        episodesAired = 2,
        airedOn = IncompleteDate(16, 8, 2020),
        releasedOn = null,
        userRate = null
    ),
    Anime(
        id = 35921,
        name = "Da Wei Bei Ken: Daomei Tegong Xiong",
        russianName = "Бернард: Агент 008",
        poster= Image("", ""),
        kind = AnimeKind.Movie,
        score = 5.43f,
        status = EntryStatus.Anons,
        episodes = 1,
        episodesAired = 0,
        airedOn = null,
        releasedOn = null,
        userRate = null
    ),
    Anime(
        id = 6823,
        name = "Omocha Bako Series Dai 3 Wa: Ehon 1936-nen",
        russianName="Момотаро против Микки Мауса",
        poster= Image("", ""),
        kind = AnimeKind.None,
        score = 4.63f,
        status = EntryStatus.None,
        episodes = 1,
        episodesAired = 0,
        airedOn = null,
        releasedOn = null,
        userRate = null
    )
)