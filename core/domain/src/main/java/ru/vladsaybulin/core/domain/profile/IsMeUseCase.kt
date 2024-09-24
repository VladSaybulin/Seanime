package ru.vladsaybulin.core.domain.profile

import kotlinx.coroutines.flow.map
import ru.vladsaybulin.data.repository.UserRepository
import javax.inject.Inject

class IsMeUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(id: Long) = userRepository.getMyIdStream().map { id == it}
}