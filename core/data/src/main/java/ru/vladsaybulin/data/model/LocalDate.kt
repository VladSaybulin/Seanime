package ru.vladsaybulin.data.model

import kotlinx.datetime.LocalDate
import ru.vladsaybulin.database.models.common.IncompleteDateEntity
import ru.vladsaybulin.model.IncompleteDate

fun LocalDate.asIncompleteDateDbo() =
    IncompleteDateEntity(
        day = this.dayOfMonth.takeIf { it != 1 },
        month = this.monthNumber.takeIf { it != 1 },
        year = this.year.takeIf { it != 1 }
    ).takeIf { it.year != null }

fun LocalDate.asIncompleteDate() =
    IncompleteDate(
        day = this.dayOfMonth.takeIf { it != 1 },
        month = this.monthNumber.takeIf { it != 1 },
        year = this.year.takeIf { it != 1 }
    ).takeIf { it.year != null }

