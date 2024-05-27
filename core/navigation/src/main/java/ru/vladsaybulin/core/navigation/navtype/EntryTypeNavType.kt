package ru.vladsaybulin.core.navigation.navtype

import android.os.Bundle
import androidx.navigation.NavType
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.common.asEntryType

object EntryTypeNavType : NavType<EntryType>(isNullableAllowed = true) {
    override fun get(bundle: Bundle, key: String): EntryType? =
        bundle.getString(key)?.let(::parseValue)

    override fun parseValue(value: String): EntryType =
        value.asEntryType()

    override fun put(bundle: Bundle, key: String, value: EntryType) {
        bundle.putString(key, value.serializedName)
    }
}