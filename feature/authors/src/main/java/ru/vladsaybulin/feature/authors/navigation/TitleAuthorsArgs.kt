package ru.vladsaybulin.feature.authors.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryType
import kotlin.reflect.typeOf

@Serializable
class TitleAuthorsArgs(
    val entryType: EntryType,
    val entryId: Long
)

val TitleAuthorsArgsNavType = mapOf(
    typeOf<EntryType>() to NavType.EnumType(EntryType::class.java)
)

fun SavedStateHandle.toTitleAuthorsArgs() = toRoute<TitleAuthorsArgs>(TitleAuthorsArgsNavType)
