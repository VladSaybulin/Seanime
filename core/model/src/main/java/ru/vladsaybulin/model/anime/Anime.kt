package ru.vladsaybulin.model.anime

import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.Image
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.userrate.UserRate

data class Anime(
    val id: Long,
    val name: String,
    val russianName: String?,
    val poster: Image?,
    val kind: AnimeKind,
    val status: EntryStatus,
    val score: Float?,
    val episodes: Int,
    val episodesAired: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?,
    val userRate: UserRate?,
)

/**
 * Preview data
 */
val previewAnimes = listOf(
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
        id = 42603,
        name = "Boku no Hero Academia: Ikinokore! Kesshi no Survival Kunren",
        russianName = null,
        poster = Image("", ""),
        kind = AnimeKind.None,
        score = 7.16f,
        status = EntryStatus.Ongoing,
        episodes = 2,
        episodesAired = 2,
        airedOn = IncompleteDate(16, 8, 2020),
        releasedOn = null,
        userRate = null
    ),
    Anime(
        id = 35575,
        name = "Nono-chan Theater",
        russianName = "Театр Ноно",
        poster = null,
        kind = AnimeKind.Ona,
        score = null,
        status = EntryStatus.None,
        episodes = 85,
        episodesAired = 0,
        airedOn = IncompleteDate(3, 9, 2001),
        releasedOn = IncompleteDate(28, 12, 2001),
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