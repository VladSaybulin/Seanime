package ru.vladsaybulin.common.network

import javax.inject.Qualifier

@Qualifier
@Retention
annotation class Dispatcher(val dispatcher: ShikiDispatchers)

enum class ShikiDispatchers {
    IO, Default
}

