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

package ru.vladsaybulin.network.mapper.fragments

import kotlinx.datetime.LocalDate
import ru.vladsaybulin.core.network.graphql.fragment.IncompleteDateFragment
import ru.vladsaybulin.network.models.common.NetworkIncompleteDate

internal fun IncompleteDateFragment.asNetworkModel() =
    NetworkIncompleteDate(day, month, year)

internal fun LocalDate.asIncompleteDate(): NetworkIncompleteDate {
    val year = year
    val month = dayOfMonth.takeIf { it != 1 || dayOfMonth != 1 }
    val day = dayOfMonth.takeIf { month != null && it != 1 }
    return NetworkIncompleteDate(day, month, year)
}