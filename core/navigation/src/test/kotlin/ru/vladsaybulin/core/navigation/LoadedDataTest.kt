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

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoadedDataTest {

    data class TestNavKey(
        val uniqueData: String,
        val ignoredData: LoadedData<String>
    )

    @Serializable
    data class DataWrapper(val data: LoadedData<String>)

    lateinit var testEntryProvider: (TestNavKey) -> NavEntry<TestNavKey>

    private var buildMetadataCount = 0

    @Before
    fun setUp() {
        buildMetadataCount = 0
        testEntryProvider = entryProvider {
            entry<TestNavKey>(
                metadata = {
                    buildMetadataCount++
                    mapOf()
                },
                content = { }
            )
        }
    }

    @Test
    fun `should lose data after serialize and deserialize LoadedData`() {
        val loadedData = LoadedData("to_lose")
        val wrapped = DataWrapper(loadedData)

        val serialized = Json.encodeToString(wrapped)

        val actualData = Json.decodeFromString<DataWrapper>(serialized).data

        assertNull(actualData.data)
    }

    @Test
    fun `entryProvider should build metadata once for keys with the same data key`() {
        invokeEntryProvider(
            key1 = TestNavKey("unique", LoadedData("data")),
            key2 = TestNavKey("unique", LoadedData("data"))
        )

        assertEquals(1, buildMetadataCount)
    }

    @Test
    fun `entryProvider should build metadata once for keys with the different ignoredData`() {
        invokeEntryProvider(
            key1 = TestNavKey("unique", LoadedData("data")),
            key2 = TestNavKey("unique", LoadedData("different_data"))
        )

        assertEquals(1, buildMetadataCount)
    }

    @Test
    fun `entryProvider should build metadata twice for keys with the different uniqueData`() {
        invokeEntryProvider(
            key1 = TestNavKey("unique", LoadedData("data")),
            key2 = TestNavKey("different_unique", LoadedData("data"))
        )

        assertEquals(2, buildMetadataCount)
    }

    @Test
    fun `entryProvider should build metadata twice for keys with the different data`() {
        invokeEntryProvider(
            key1  = TestNavKey("unique", LoadedData("data")),
            key2 = TestNavKey("different_unique", LoadedData("different_data"))
        )

        assertEquals(2, buildMetadataCount)
    }

    fun invokeEntryProvider(
        key1: TestNavKey,
        key2: TestNavKey
    ) {
        testEntryProvider.invoke(key1)
        testEntryProvider.invoke(key2)
    }
}