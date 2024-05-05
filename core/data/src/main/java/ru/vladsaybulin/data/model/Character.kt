package ru.vladsaybulin.data.model

import ru.vladsaybulin.core.textprocessor.html.HtmlToAnnotatedTextTransformer
import ru.vladsaybulin.database.models.character.CharacterAnimeCrossRef
import ru.vladsaybulin.database.models.character.CharacterDetailsEntity
import ru.vladsaybulin.database.models.character.CharacterEntity
import ru.vladsaybulin.database.models.character.CharacterMangaCrossRef
import ru.vladsaybulin.database.models.character.CharacterSeyuCrossRef
import ru.vladsaybulin.model.character.Character
import ru.vladsaybulin.model.character.CharacterWithRole
import ru.vladsaybulin.network.models.character.NetworkCharacter
import ru.vladsaybulin.network.models.character.NetworkCharacterDetails
import ru.vladsaybulin.network.models.character.NetworkCharacterWithRole

fun NetworkCharacterWithRole.asExternalModel() = CharacterWithRole(
    character = character.asExternalModel(),
    isMain = isMain
)

fun NetworkCharacter.asExternalModel() = Character(
    id = id,
    originalName = name,
    russianName = nameRu,
    poster = image?.asExternalModel()
)

fun NetworkCharacter.asEntity() = CharacterEntity(
    id = id,
    name = name,
    nameRu = nameRu,
    image = image?.asPOJO()
)

fun NetworkCharacterDetails.asEntity() = CharacterEntity(
    id = id,
    name = name,
    nameRu = nameRu,
    image = image?.asPOJO()
)

fun NetworkCharacterDetails.asDetailsEntity(htmlToAnnotatedTextTransformer: HtmlToAnnotatedTextTransformer) = CharacterDetailsEntity(
    id = id,
    nameJp = nameJp,
    altNames = alternativeName,
    description = descriptionHtml?.toAnnotatedTextPOJO(htmlToAnnotatedTextTransformer),
    descriptionSource = descriptionSource,
    topicId = topicId,
    updatedAt = updatedAt
)

fun NetworkCharacterDetails.personEntityShells() = seyu.map { it.asEntity() }

fun NetworkCharacterDetails.seyuCrossRefs() = seyu.map {
    CharacterSeyuCrossRef(id, it.id)
}

fun NetworkCharacterDetails.animeEntityShells() = animes.map { it.asEntity() }

fun NetworkCharacterDetails.animeCrossRefs() = animes.map {
    CharacterAnimeCrossRef(id, it.id)
}

fun NetworkCharacterDetails.mangaEntityShells() = mangas.map { it.asEntity() }

fun NetworkCharacterDetails.mangaCrossRefs() = mangas.map {
    CharacterMangaCrossRef(id, it.id)
}