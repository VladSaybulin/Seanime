package ru.vladsaybulin.model.manga

enum class MangaKind(val serializedName: String) {
    Manga("manga"),
    Manhwa("manhwa"),
    Manhua("manhua"),
    OneShot("one_shot"),
    Doujin("doujin"),

    //Ranobe kind
    LightNovel("light_novel"),
    Novel("novel"),

    None("")
}

val mangaKind = listOf(
    MangaKind.Manga,
    MangaKind.Manhwa,
    MangaKind.Manhua,
    MangaKind.OneShot,
    MangaKind.Doujin
)

val ranobeKind = listOf(
    MangaKind.LightNovel,
    MangaKind.Novel
)

fun String?.asMangaKind() = when (this) {
    null -> MangaKind.None
    else -> MangaKind.entries.firstOrNull { it.serializedName == this } ?: MangaKind.None
}
