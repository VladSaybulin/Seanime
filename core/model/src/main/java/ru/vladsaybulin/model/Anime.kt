package ru.vladsaybulin.model

data class Anime(
    val id: Long,
    val originalName: String,
    val russianName: String?,
    val poster: Poster?,
    val kind: AnimeKind,
    val status: EntryStatus,
    val score: Float?,
    val episodes: Int,
    val episodesAired: Int,
    val airedOn: IncompleteDate?,
    val releasedOn: IncompleteDate?
)

/**
 * Preview data
 */
val previewAnimes = listOf(
    Anime(
        id = 17201,
        originalName = "Toki no Daichi: Hana no Oukoku no Majo",
        russianName = "Земля времени: Ведьма королевства цветов",
        poster = Poster("https://desu.shikimori.one/uploads/poster/animes/17201/cad687fc14b233c1503518de4ab888f2.jpeg"),
        kind = AnimeKind.Ova,
        score = 5.8f,
        status = EntryStatus.Released,
        episodes = 3,
        episodesAired = 0,
        airedOn = IncompleteDate(22, 12, 1998),
        releasedOn = IncompleteDate(25, 3, 1999)
    ),
    Anime(
        id = 42603,
        originalName = "Boku no Hero Academia: Ikinokore! Kesshi no Survival Kunren",
        russianName = null,
        poster = Poster("https://desu.shikimori.one/uploads/poster/animes/42603/e578c38eff69123948e5f1165d1ec061.jpeg"),
        kind = AnimeKind.None,
        score = 7.16f,
        status = EntryStatus.Ongoing,
        episodes = 2,
        episodesAired = 2,
        airedOn = IncompleteDate(16, 8, 2020),
        releasedOn = null,
    ),
    Anime(
        id = 35575,
        originalName = "Nono-chan Theater",
        russianName = "Театр Ноно",
        poster = null,
        kind = AnimeKind.Ona,
        score = null,
        status = EntryStatus.None,
        episodes = 85,
        episodesAired = 0,
        airedOn = IncompleteDate(3, 9, 2001),
        releasedOn = IncompleteDate(28, 12, 2001)
    ),
    Anime(
        id = 35921,
        originalName = "Da Wei Bei Ken: Daomei Tegong Xiong",
        russianName = "Бернард: Агент 008",
        poster= Poster("https://desu.shikimori.one/uploads/poster/animes/35921/51c4332d69cbb9b80dfa6084a916c7c0.jpeg"),
        kind = AnimeKind.Movie,
        score = 5.43f,
        status = EntryStatus.Anons,
        episodes = 1,
        episodesAired = 0,
        airedOn = null,
        releasedOn = null,
    ),
    Anime(
        id = 6823,
        originalName = "Omocha Bako Series Dai 3 Wa: Ehon 1936-nen",
        russianName="Момотаро против Микки Мауса",
        poster= Poster("https://desu.shikimori.one/uploads/poster/animes/6823/b21ce53a5e9d18a80949f2ad156e171c.jpeg"),
        kind = AnimeKind.None,
        score = 4.63f,
        status = EntryStatus.None,
        episodes = 1,
        episodesAired = 0,
        airedOn = null,
        releasedOn = null
    )
)