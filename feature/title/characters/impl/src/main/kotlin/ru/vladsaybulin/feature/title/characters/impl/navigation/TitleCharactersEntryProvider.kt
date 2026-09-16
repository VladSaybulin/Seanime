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

package ru.vladsaybulin.feature.title.characters.impl

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import ru.vladsaybulin.core.navigation.Navigator
import ru.vladsaybulin.core.navigation.SeanimeNavKey
import ru.vladsaybulin.feature.character.api.navigation.navigateToCharacter
import ru.vladsaybulin.feature.title.characters.api.navigation.TitleCharactersNavKey

context(navigator: Navigator)
fun EntryProviderScope<SeanimeNavKey>.titleCharactersEntry() = entry<TitleCharactersNavKey> { key ->
    val viewModel = hiltViewModel<TitleCharacterViewModel, TitleCharacterViewModel.Factory> {
        it.create(key)
    }

    TitleCharactersRoute(
        viewModel = viewModel,
        onCharacterClick = navigator::navigateToCharacter,
        onBackClick = navigator::back
    )
}