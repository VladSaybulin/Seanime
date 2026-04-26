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

package ru.vladsaybulin.data.model

import kotlinx.datetime.LocalDate
import ru.vladsaybulin.database.models.common.IncompleteDatePOJO
import ru.vladsaybulin.model.common.IncompleteDate

fun LocalDate.asIncompleteDateDbo() =
    IncompleteDatePOJO(
        day = this.dayOfMonth.takeIf { it != 1 },
        month = this.monthNumber.takeIf { it != 1 },
        year = this.year.takeIf { it != 1 }
    ).takeIf { it.year != null }

fun LocalDate.asExternalModel() =
    IncompleteDate(
        day = this.dayOfMonth.takeIf { it != 1 },
        month = this.monthNumber.takeIf { it != 1 },
        year = this.year.takeIf { it != 1 }
    ).takeIf { it.year != null }

