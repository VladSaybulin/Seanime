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