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

package ru.vladsaybulin.feature.title.details.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.list.title.details.navigation.TitleDetailsNavKey
import ru.vladsaybulin.feature.title.details.impl.TitleDetailsScreen
import ru.vladsaybulin.feature.title.details.impl.TitleDetailsViewModel

context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.titleDetailsEntry() = entry<TitleDetailsNavKey> { key ->
    val viewModel = hiltViewModel<TitleDetailsViewModel, TitleDetailsViewModel.Factory> {
        it.create(key)
    }

    TitleDetailsScreen(
        viewModel = viewModel,
        navigator = rememberTitleDetailsNavigator(navigator, key)
    )
}