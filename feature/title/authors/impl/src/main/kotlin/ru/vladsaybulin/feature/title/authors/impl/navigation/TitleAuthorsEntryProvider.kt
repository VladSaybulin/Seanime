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

package ru.vladsaybulin.feature.title.authors.impl.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.title.authors.api.navigation.TitleAuthorsNavKey
import ru.vladsaybulin.feature.title.authors.impl.TitleAuthorsScreen
import ru.vladsaybulin.feature.title.authors.impl.TitleAuthorsViewModel

context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.titleAuthorsEntry() = entry<TitleAuthorsNavKey> { key ->
    val viewModel = hiltViewModel<TitleAuthorsViewModel, TitleAuthorsViewModel.Factory> {
        it.create(key)
    }

    TitleAuthorsScreen(
        viewModel = viewModel,
        onPersonClick = { /* TODO navigator::navigateToPerson */ },
        onBackClick = navigator::back
    )
}