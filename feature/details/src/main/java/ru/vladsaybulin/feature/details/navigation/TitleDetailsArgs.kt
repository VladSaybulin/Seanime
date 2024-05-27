package ru.vladsaybulin.feature.details.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.vladsaybulin.model.common.EntryType
import kotlin.reflect.typeOf

@Serializable
data class TitleDetailsArgs(
    val titleType: EntryType,
    val titleId: Long
)


val TitleDetailsNavType= mapOf(
    typeOf<EntryType>() to NavType.EnumType(EntryType::class.java)
)

internal fun SavedStateHandle.toTitleDetailsScreenArgs() =
    toRoute<TitleDetailsArgs>(TitleDetailsNavType)