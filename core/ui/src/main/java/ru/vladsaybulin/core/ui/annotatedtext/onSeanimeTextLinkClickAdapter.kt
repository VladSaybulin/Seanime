package ru.vladsaybulin.core.ui.annotatedtext

fun <Action> onSeanimeTextLinkClickAdapter(
    onAnimeClick: (Long) -> Action?,
    onMangaClick: (Long) -> Action?,
    onCharacterClick: (Long) -> Action?,
    onPersonClick: (Long) -> Action?,
    onUrlClick: (String) -> Action?,
    onAction: (Action) -> Unit
): (String, String) -> Unit = { tag, annotation ->
    val action = when (tag) {
        "anime" -> onAnimeClick(annotation.toLong())
        "manga", "ranobe" -> onMangaClick(annotation.toLong())
        "character" -> onCharacterClick(annotation.toLong())
        "person" -> onPersonClick(annotation.toLong())
        "url" -> onUrlClick(annotation)
        else -> null
    }
    action?.let(onAction)
}