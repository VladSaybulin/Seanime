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

package ru.vladsaybulin.core.navigation

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class AbstractRouteWithArgumentsSerializer<Args : Any, Route : RouteWithArguments<Args>>(
    private val argsSerializer: KSerializer<Args>
) : KSerializer<Route> {

    @OptIn(ExperimentalSerializationApi::class)
    final override val descriptor: SerialDescriptor = SerialDescriptor(
        serialName = this::class.qualifiedName ?: "",
        original = argsSerializer.descriptor
    )

    final override fun deserialize(decoder: Decoder): Route {
        error("Unsupported deserialize operation RouteWithArguments")
    }

    final override fun serialize(encoder: Encoder, value: Route) {
        argsSerializer.serialize(encoder, value.args)
    }
}