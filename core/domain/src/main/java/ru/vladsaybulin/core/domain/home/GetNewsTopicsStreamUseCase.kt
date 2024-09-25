package ru.vladsaybulin.core.domain.home

import ru.vladsaybulin.data.repository.TopicsRepository
import javax.inject.Inject

class GetNewsTopicsStreamUseCase @Inject constructor(private val topicsRepository: TopicsRepository) {
    operator fun invoke() = topicsRepository.getNewsTopicsStream()

}