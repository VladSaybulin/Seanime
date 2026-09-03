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

package ru.vladsaybulin.data

import kotlinx.datetime.DatePeriod
import ru.vladsaybulin.data.request.DefaultTTLStrategy
import ru.vladsaybulin.data.request.NextDayMidnightTTLStrategy
import ru.vladsaybulin.data.request.TTLStrategy
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * Predefined TTL policies for app data groups.
 */
object TTLStrategies {

    val ForceRefresh = TTLStrategy { _, _ -> true }

    /** Character details are refreshed at most once per day by default. */
    val CharacterDetails = DefaultTTLStrategy(ttl = 1.days)

    /** Title details are refreshed hourly by default. */
    val TitleDetails = DefaultTTLStrategy(ttl = 1.hours)

    /** Calendar is refreshed after day rollover. */
    val Calendar = NextDayMidnightTTLStrategy(DatePeriod(days = 1))

    /** In-progress user rates have a short cache window. */
    val InProgressRates = DefaultTTLStrategy(5.minutes)

    /** News topics are refreshed hourly by default. */
    val News = DefaultTTLStrategy(1.hours)

    /** Ongoing anime list is refreshed after day rollover. */
    val OngoingAnimes = NextDayMidnightTTLStrategy(DatePeriod(days = 1))
}

inline fun withForceStrategy(force: Boolean, block: () -> TTLStrategy) = if (force) {
    TTLStrategies.ForceRefresh
} else {
    block()
}
