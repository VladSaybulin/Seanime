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

fun String?.asMangaKind() = when (this) {
    null -> MangaKind.None
    else -> MangaKind.entries.firstOrNull { it.serializedName == this } ?: MangaKind.None
}
