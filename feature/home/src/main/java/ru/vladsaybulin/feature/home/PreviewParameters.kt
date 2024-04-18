package ru.vladsaybulin.feature.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.datetime.Clock
import ru.vladsaybulin.model.anime.Anime
import ru.vladsaybulin.model.anime.AnimeKind
import ru.vladsaybulin.model.common.EntryStatus
import ru.vladsaybulin.model.common.IncompleteDate
import ru.vladsaybulin.model.manga.Manga
import ru.vladsaybulin.model.manga.MangaKind
import ru.vladsaybulin.model.common.Poster
import ru.vladsaybulin.model.userrate.UserRate
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import ru.vladsaybulin.model.topic.NewsTopic
import ru.vladsaybulin.model.user.BriefUser
import ru.vladsaybulin.model.user.UserImage

val previewAnimes = listOf(
    Anime(
        id = 16498,
        name = "Shingeki no Kyojin",
        russianName = "Атака титанов",
        poster = Poster("url"),
        kind = AnimeKind.Tv,
        score = 8.54f,
        status = EntryStatus.Released,
        episodes = 25,
        episodesAired = 25,
        airedOn = IncompleteDate(7, 4, 2013),
        releasedOn = IncompleteDate(29, 9, 2013),
        userRate = null
    ),
    Anime(
        id = 1535,
        name = "Death Note",
        russianName = "Тетрадь смерти",
        poster = Poster(originalUrl = "url"),
        kind = AnimeKind.Tv,
        score = 8.62f,
        status = EntryStatus.Released,
        episodes = 37,
        episodesAired = 0,
        airedOn = IncompleteDate(day = 4, month = 10, year = 2006),
        releasedOn = IncompleteDate(day = 27, month = 6, year = 2007),
        userRate = null
    ),
    Anime(
        id = 5114,
        name = "Fullmetal Alchemist: Brotherhood",
        russianName = "Стальной алхимик: Братство",
        poster = Poster("url"),
        kind = AnimeKind.Tv,
        score = 9.09f,
        status = EntryStatus.Released,
        episodes = 64,
        episodesAired = 0,
        airedOn = IncompleteDate(day = 5, month = 4, year = 2009),
        releasedOn = IncompleteDate(day = 4, month = 7, year = 2010),
        userRate = null
    ),
    Anime(
        id = 30276,
        name = "One Punch Man",
        russianName = "Ванпанчмен",
        poster = Poster(
            originalUrl = "https://desu.shikimori.one/uploads/poster/animes/30276/1c07fa5cce615e2a2b85be3c3695ed73.jpeg",
            previewUrl = "https://desu.shikimori.one/uploads/poster/animes/30276/preview-ce8600d3027776312e4e2d16c8f04629.webp"
        ),
        kind = AnimeKind.Tv,
        score = 8.5f,
        status = EntryStatus.Released,
        episodes = 12,
        episodesAired = 12,
        airedOn = IncompleteDate(day = 5, month = 10, year = 2015),
        releasedOn = IncompleteDate(day = 21, month = 12, year = 2015),
        userRate = null
    ),
    Anime(
        id = 11757,
        name = "Sword Art Online",
        russianName = "Мастера Меча Онлайн",
        poster = Poster(
            originalUrl = "https://desu.shikimori.one/uploads/poster/animes/11757/8958e24041338f53bdab4955ed395d66.jpeg",
            previewUrl = "https://desu.shikimori.one/uploads/poster/animes/11757/preview-211fcfe59648bad484ca8560b55a2d48.webp"
        ),
        kind = AnimeKind.Tv,
        score = 7.21f,
        status = EntryStatus.Released,
        episodes = 25,
        episodesAired = 25,
        airedOn = IncompleteDate(day = 8, month = 7, year = 2012),
        releasedOn = IncompleteDate(day = 23, month = 12, year = 2012),
        userRate = null
    )
)

val previewMangas = listOf(
    Manga(
        id = 2,
        name = "Berserk",
        russianName = "Берсерк",
        poster = Poster("url"),
        kind = MangaKind.Manga,
        score = 9.47f,
        status = EntryStatus.Ongoing,
        chapters = 0,
        volumes = 0,
        airedOn = IncompleteDate(25, 8, 1989),
        releasedOn = IncompleteDate(null, null, null)
    ),
    Manga(
        id = 23390,
        name = "Shingeki no Kyojin ",
        russianName = "Атака титанов",
        poster = Poster("url"),
        kind = MangaKind.Manga,
        score = 8.55f,
        status = EntryStatus.Released,
        chapters = 141,
        volumes = 34,
        airedOn = IncompleteDate(9, 9, 2009),
        releasedOn = IncompleteDate(9, 4, 2021)
    ),
    Manga(
        id = 13,
        name = "One Piece",
        russianName = "Ван-Пис",
        poster = Poster("url"),
        kind = MangaKind.Manga,
        score = 9.22f,
        status = EntryStatus.Ongoing,
        chapters = 0,
        volumes = 0,
        airedOn = IncompleteDate(22, 7, 1997),
        releasedOn = IncompleteDate(null, null, null)
    ),
    Manga(
        id = 116778,
        name = "Chainsaw Man",
        russianName = "Человек-бензопила",
        poster = Poster("url"),
        kind = MangaKind.Manga,
        score = 8.73f,
        status = EntryStatus.Ongoing,
        chapters = 0,
        volumes = 0,
        airedOn = IncompleteDate(3, 12, 2018),
        releasedOn = IncompleteDate(null, null, null)
    ),
    Manga(
        id = 33327,
        name = "Tokyo Ghoul",
        russianName = "Токийский гуль",
        poster = Poster("url"),
        kind = MangaKind.Manga,
        score = 8.53f,
        status = EntryStatus.Released,
        chapters = 144,
        volumes = 14,
        airedOn = IncompleteDate(8, 9, 2011),
        releasedOn = IncompleteDate(18, 9, 2014)
    )
)

val previewUserRates = listOf(
    UserRateWithEntry(
        anime = previewAnimes[0],
        userRate = UserRate(
            id = 1,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            status = UserRateStatus.Watching,
            score = 0,
            episodes = 3,
            chapters = 0,
            volumes = 0,
            rewatches = 0,
            text = ""
        )
    ),
    UserRateWithEntry(
        manga = previewMangas[2],
        userRate = UserRate(
            id = 2,
            createdAt = Clock.System.now(),
            updatedAt = Clock.System.now(),
            status = UserRateStatus.Watching,
            score = 10,
            episodes = 0,
            chapters = 1111,
            volumes = 0,
            rewatches = 0,
            text = ""
        )
    )
)

val previewNewsTopics = listOf(
    NewsTopic(
        title = "Title 1",
        imageUrl = "",
        createdAt = Clock.System.now(),
        commentsCount = 48,
        user = BriefUser(
            id = 1,
            nickname = "nickname",
            avatarUrl = "url",
            image = UserImage("", "", "", "", "", "", ""),
            lastOnlineAt = Clock.System.now(),
            url = ""
        ),
    ),
    NewsTopic(
        title = "Title 2 without image",
        imageUrl = null,
        createdAt = Clock.System.now(),
        commentsCount = 48,
        user = BriefUser(
            id = 1,
            nickname = "nickname",
            avatarUrl = "url",
            image = UserImage("", "", "", "", "", "", ""),
            lastOnlineAt = Clock.System.now(),
            url = ""
        ),
    )
)

public class UserRatePreviewParameterProvider : PreviewParameterProvider<UserRateWithEntry> {
    override val values: Sequence<UserRateWithEntry>
        get() = previewUserRates.asSequence()

    override val count: Int
        get() = previewUserRates.size

}