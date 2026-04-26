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

package ru.vladsaybulin.database.models.userrate

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField

@Entity(tableName = "paged_user_rates")
class PagedUserRateEntity(

    @ColumnInfo("user_rate_id")
    @PrimaryKey
    val userRateId: Long,

    @ColumnInfo("order_field")
    val orderField: UserRateOrderField,

    @ColumnInfo("sort_order")
    val order: UserRateOrder,

    @ColumnInfo("page")
    val page: Int,

    @ColumnInfo("index")
    val index: Int
)