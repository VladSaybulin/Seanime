package ru.vladsaybulin.core.domain.profile

import kotlinx.coroutines.flow.Flow
import ru.vladsaybulin.data.repository.UserRepository
import ru.vladsaybulin.model.user.BriefUser
import javax.inject.Inject

class GetBriefUserStreamUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: Long?): Flow<BriefUser?> =
        if (userId == null) {
            userRepository.getMeStream()
        } else {
            userRepository.getUserStream(userId)
        }
}