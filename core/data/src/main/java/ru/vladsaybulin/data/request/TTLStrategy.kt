/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vladsaybulin.data.request

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toDateTimePeriod
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days

fun interface TTLStrategy {
    fun isExpired(now: Instant, lastRequest: Instant): Boolean
}

class DefaultTTLStrategy(private val ttl: Duration) : TTLStrategy {
    override fun isExpired(now: Instant, lastRequest: Instant): Boolean {
        return now - lastRequest >= ttl
    }
}

class NextDayMidnightTTLStrategy(
    private val datePeriod: DatePeriod
) : TTLStrategy {

    override fun isExpired(now: Instant, lastRequest: Instant): Boolean {
        val timeZone: TimeZone = TimeZone.currentSystemDefault()

        val lastDate = lastRequest.toLocalDateTime(timeZone).date
        val nextDay = lastDate + datePeriod

        val refreshAt = nextDay.atStartOfDayIn(timeZone)

        return now >= refreshAt
    }
}