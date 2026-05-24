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
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

object TTLStrategies {
    val CharacterDetails = DefaultTTLStrategy(ttl = 1.days)
    val TitleDetails = DefaultTTLStrategy(ttl = 1.hours)
    val Calendar = NextDayMidnightTTLStrategy(DatePeriod(days = 1))
    val InProgressRates = DefaultTTLStrategy(5.minutes)
    val News = DefaultTTLStrategy(1.hours)
    val OngoingAnimes = NextDayMidnightTTLStrategy(DatePeriod(days = 1))
}