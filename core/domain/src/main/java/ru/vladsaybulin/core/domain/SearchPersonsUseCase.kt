package ru.vladsaybulin.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import ru.vladsaybulin.common.network.Dispatcher
import ru.vladsaybulin.common.network.ShikiDispatchers.Default
import ru.vladsaybulin.model.person.PersonWithRoles
import javax.inject.Inject

class SearchPersonsUseCase @Inject constructor(
    @Dispatcher(Default) private val defaultDispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        authors: List<PersonWithRoles>,
        searchQuery: String
    ): List<PersonWithRoles> {
        if (searchQuery.isBlank()) return authors
        return authors.filter {
            with(it.person) {
                originalName.contains(searchQuery, ignoreCase = true) ||
                        (russianName != null &&
                                russianName!!.contains(searchQuery, ignoreCase = true))
            }
        }
    }
}