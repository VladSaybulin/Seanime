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

