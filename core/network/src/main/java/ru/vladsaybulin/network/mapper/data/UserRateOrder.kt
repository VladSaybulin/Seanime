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

package ru.vladsaybulin.network.mapper.data

import ru.vladsaybulin.core.network.graphql.type.UserRateOrderInputType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.network.mapper.enums.asSortOrderEnum
import ru.vladsaybulin.network.mapper.enums.asUserRateOrderFieldEnum

fun Pair<UserRateOrderField, UserRateOrder>.asUserRateOrderInputType() = UserRateOrderInputType(
    field = first.asUserRateOrderFieldEnum(),
    order = second.asSortOrderEnum()
)
