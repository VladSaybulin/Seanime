/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.core.navigation

import kotlinx.serialization.Serializable
import ru.vladsaybulin.core.navigation.utils.LoadedDataSerializer

/**
 * A wrapper class for [data] to be ignored by navigation
 * The goals of this wrapper class
 * - [data] is excluded from serialization or converted to a generic format (example empty string or "LoadedData(*)").
 * - [data] are ignored in equals with other data, including maps.
 * When using [LoadedData] in NavKeys, it is recommended to mark them with a [@Transient] annotation
 * that excludes them from serialization, even if a default serializer is provided for them.
 * Before using this class, you should consider the procedure for restoring this data and these repositories.
 */
@Serializable(with = LoadedDataSerializer::class)
class LoadedData<T>(val data: T?) {
    override fun toString(): String {
        return "LoadedData(*)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is LoadedData<*>
    }

    override fun hashCode(): Int {
        return LoadedData::class.hashCode()
    }

    companion object {
        inline fun <reified T> ofNull(): LoadedData<T> {
            return LoadedData(null)
        }
    }
}