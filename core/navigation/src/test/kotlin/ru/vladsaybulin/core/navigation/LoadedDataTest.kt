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

/**
 * The test covers two main guarantees:
 * 1) Serialization drops the wrapped value.
 * The test serializes and deserializes a [LoadedData] instance
 * and checks that the restored [LoadedData] no longer contains the original payload.
 * This confirms that [LoadedData] behaves as a non-persistent container for navigation state.
 * 2) Navigation key identity is not affected by the wrapped value.
 * The test uses a [TestNavKey] data class
 * It then checks metadata generation for different combinations:
 *      - same [TestNavKey.uniqueData], same [TestNavKey.ignoredData] → metadata is built once
 *      - same [TestNavKey.uniqueData], different [TestNavKey.ignoredData] → metadata is still built once
 *      - different [TestNavKey.uniqueData] → metadata is built twice
 *      - different [TestNavKey.uniqueData] and different [TestNavKey.ignoredData] → metadata is built twice
 *
 * This confirms that `LoadedData` does not affect key identity and does not break metadata reuse inside the entry provider.
 */
class LoadedDataTest {

    /**
     * @param uniqueData It real navigation key that should affect navigation identity
     * @param ignoredData It is a wrapped value that should not affect navigation identity
     */
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
            unique = "unique",
            ignored = "ignored"
        )

        assertEquals(1, buildMetadataCount)
    }

    @Test
    fun `entryProvider should build metadata once for keys with the different ignoredData`() {
        invokeEntryProvider(
            unique = "unique",
            ignored = "ignored",
            differentIgnored = "different_data"
        )

        assertEquals(1, buildMetadataCount)
    }

    @Test
    fun `entryProvider should build metadata twice for keys with the different uniqueData`() {
        invokeEntryProvider(
            unique = "unique",
            ignored = "ignored",
            differentUnique = "different_data"
        )

        assertEquals(2, buildMetadataCount)
    }

    @Test
    fun `entryProvider should build metadata twice for keys with the different data`() {
        invokeEntryProvider(
            unique = "unique",
            ignored = "ignored",
            differentUnique = "different_data",
            differentIgnored = "different_data"
        )

        assertEquals(2, buildMetadataCount)
    }

    fun invokeEntryProvider(
        unique: String,
        ignored: String,
        differentUnique: String? = null,
        differentIgnored: String? = null
    ) {
        val key1 = TestNavKey(unique, LoadedData(ignored))
        val key2 = TestNavKey(differentUnique ?: unique, LoadedData(differentIgnored ?: ignored))
        testEntryProvider.invoke(key1)
        testEntryProvider.invoke(key2)
    }
}