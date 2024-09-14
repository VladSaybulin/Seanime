package ru.vladsaybulin.core.domain.titledetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.vladsaybulin.core.auth.ShikimoriAuthorization
import ru.vladsaybulin.data.repository.UserRateRepository
import ru.vladsaybulin.model.auth.ShikimoriAuthState
import ru.vladsaybulin.model.common.EntryType
import ru.vladsaybulin.model.userrate.UserRate
import javax.inject.Inject

class GetUserRateScreamUseCase @Inject constructor(
    private val auth: ShikimoriAuthorization,
    private val userRateRepository: UserRateRepository
) {
    operator fun invoke(titleType: EntryType, titleId: Long): Flow<UserRateResult> =
        auth.shikimoriAuthState.flatMapLatest {
            if (it == ShikimoriAuthState.LOGGED_IN) {
                when (titleType) {
                    EntryType.Anime -> userRateRepository.getAnimeUserRateStream(titleId)
                    EntryType.Manga -> userRateRepository.getMangaUserRateStream(titleId)
                }.map(UserRateResult::Success)
            } else flowOf(UserRateResult.NotAuthorized)
        }
}

sealed class UserRateResult {
    data object NotAuthorized : UserRateResult()
    data class Success(val userRate: UserRate?) : UserRateResult()
}