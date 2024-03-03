package ru.vladsaybulin.model

enum class UserRateStatus(val serializedName: String) {
    Planned("planned"),
    Watching("watching"),
    Rewatching("rewathing"),
    Completed("completed"),
    OnHold("on_hold"),
    Dropped("dropped"),
    None("")
}