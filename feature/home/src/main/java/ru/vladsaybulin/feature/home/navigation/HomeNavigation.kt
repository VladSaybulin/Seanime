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

package ru.vladsaybulin.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.home.HomeScreen

@Serializable
object HomeGraphRoute

@Serializable
private object HomeScreenRoute

fun NavController.navigateToHomeGraph(navOptions: NavOptions?) {
    navigate(HomeGraphRoute, navOptions)
}

fun NavGraphBuilder.homeGraph(
    navEvents: HomeNavEvents,
    nested: NavGraphBuilder.() -> Unit
) {
    navigation<HomeGraphRoute>(startDestination = HomeScreenRoute) {
        composable<HomeScreenRoute> {
            HomeScreen(navEvents)
        }
        nested()
    }
}