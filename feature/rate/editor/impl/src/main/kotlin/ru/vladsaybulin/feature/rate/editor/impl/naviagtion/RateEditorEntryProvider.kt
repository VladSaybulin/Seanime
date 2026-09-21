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

package ru.vladsaybulin.feature.rate.editor.impl.naviagtion

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.core.navigation.scene.BottomSheetSceneStrategy
import ru.vladsaybulin.feature.rate.editor.api.navigation.RateEditorNavKey
import ru.vladsaybulin.feature.rate.editor.impl.RateEditorSheet
import ru.vladsaybulin.feature.rate.editor.impl.RateEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.rateEditorEntry() = entry<RateEditorNavKey>(
    metadata = BottomSheetSceneStrategy.bottomSheet(true)
) { key ->
    val viewModel = hiltViewModel<RateEditorViewModel, RateEditorViewModel.Factory> {
        it.create(key)
    }

    RateEditorSheet(
        viewModel = viewModel,
        onCloseRequest = navigator::back
    )
}