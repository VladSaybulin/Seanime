package ru.vladsaybulin.network.retrofit

import retrofit2.http.GET
import ru.vladsaybulin.network.retrofit.models.CalendarDto

interface ShikiUnauthorizedApi {
    @GET("/api/calendar")
    suspend fun getAllCalendarItems(): List<CalendarDto>
}
