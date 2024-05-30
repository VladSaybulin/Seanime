package ru.vladsaybulin.core.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.list.UserRateOrder
import ru.vladsaybulin.model.list.UserRateOrderField
import ru.vladsaybulin.model.userrate.UserRateStatus
import ru.vladsaybulin.model.userrate.UserRateWithEntry
import javax.inject.Inject

class GetPagedUserRatesUseCase @Inject constructor(
    private val userRateRepository: UserRateRepository
) {
    operator fun invoke(
        entryType: EntryType,
        userRateStatus: UserRateStatus,
        orderField: UserRateOrderField,
        order: UserRateOrder,
    ): Flow<PagingData<UserRateWithEntry>> =
        when (entryType) {
            EntryType.Anime -> userRateRepository.getPagedAnimeUserRates(userRateStatus, orderField, order)
            EntryType.Manga -> userRateRepository.getPagedMangaUserRates(userRateStatus, orderField, order)
        }
}