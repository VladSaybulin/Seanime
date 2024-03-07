package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.network.graphql.type.MangaKindEnum
import ru.vladsaybulin.model.MangaKind

fun MangaKindEnum?.asMangaKind() = when (this) {
    MangaKindEnum.manga -> MangaKind.Manga
    MangaKindEnum.manhwa -> MangaKind.Manhwa
    MangaKindEnum.manhua -> MangaKind.Manhua
    MangaKindEnum.light_novel -> MangaKind.LightNovel
    MangaKindEnum.novel -> MangaKind.Novel
    MangaKindEnum.one_shot -> MangaKind.OneShot
    MangaKindEnum.doujin -> MangaKind.Doujin
    else -> MangaKind.None
}