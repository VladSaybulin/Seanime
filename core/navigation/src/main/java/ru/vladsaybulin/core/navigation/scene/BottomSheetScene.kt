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
@file:OptIn(ExperimentalMaterial3Api::class)

package ru.vladsaybulin.core.navigation.scene

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope

data class BottomSheetScene<T : Any>(
    override val key: Any,
    val entry: NavEntry<T>,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    val properties: ModalBottomSheetProperties,
    val skipPartiallyExpanded: Boolean,
    val onBack: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(entry)

    lateinit var sheetState: SheetState

    override val content = @Composable {
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
        val lifecycleOwner = rememberLifecycleOwner()

        ModalBottomSheet(
            onDismissRequest = onBack,
            sheetState = sheetState,
            properties = properties,
            modifier = Modifier.heightIn(min = 200.dp)
        ) {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                entry.Content()
            }
        }
    }

    override suspend fun onRemove() {
        sheetState.hide()
    }
}

class BottomSheetSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val data = lastEntry.metadata[BottomSheetKey] ?: return null

        return BottomSheetScene(
            key = lastEntry.contentKey,
            entry = lastEntry,
            previousEntries = entries.dropLast(1),
            overlaidEntries = entries.dropLast(1),
            properties = data.properties,
            skipPartiallyExpanded = data.skipPartiallyExpanded,
            onBack = onBack
        )
    }

    companion object {
        object BottomSheetKey : NavMetadataKey<BottomSheetMetadata>

        fun bottomSheet(
            skipPartiallyExpanded: Boolean = false,
            bottomSheetProperties: ModalBottomSheetProperties = ModalBottomSheetProperties(),
        ): Map<String, Any> =
            metadata {
                put(BottomSheetKey, BottomSheetMetadata(skipPartiallyExpanded, bottomSheetProperties))
            }
    }

    data class BottomSheetMetadata(
        val skipPartiallyExpanded: Boolean = false,
        val properties: ModalBottomSheetProperties = ModalBottomSheetProperties()
    )
}