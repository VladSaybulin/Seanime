package ru.vladsaybulin.network.datasource

import retrofit2.Retrofit
import retrofit2.create
import retrofit2.http.GET
import ru.vladsaybulin.network.models.CalendarItemDto
import javax.inject.Inject

interface CalendarApi {
    @GET("api/calendar")
    suspend fun getAllCalendarItems(): List<CalendarItemDto>
}

class CalendarDataSource @Inject internal constructor(retrofit: Retrofit) {
    private val api: CalendarApi = retrofit.create()

    suspend fun getAllCalendarItems(): List<CalendarItemDto> = api.getAllCalendarItems()
}