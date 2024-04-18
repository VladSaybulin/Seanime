package ru.vladsaybulin.model.userrate

@JvmInline
value class UserRateProgressLimit(val limit: Int) {
    companion object {
        val Unlimited = UserRateProgressLimit(Int.MAX_VALUE)
        val Unspecified = UserRateProgressLimit(-1)
    }
}

fun Int.toLimit() = UserRateProgressLimit(this)