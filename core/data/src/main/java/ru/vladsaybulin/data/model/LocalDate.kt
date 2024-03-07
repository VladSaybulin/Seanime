package ru.vladsaybulin.data.model

import kotlinx.datetime.LocalDate
import ru.vladsaybulin.database.models.IncompleteDateDbo

fun LocalDate.asIncompleteDateDbo() =
    IncompleteDateDbo(
        day = this.dayOfMonth.takeIf { it != 1 },
        month = this.monthNumber.takeIf { it != 1 },
        year = this.year.takeIf { it != 1 }
    )
