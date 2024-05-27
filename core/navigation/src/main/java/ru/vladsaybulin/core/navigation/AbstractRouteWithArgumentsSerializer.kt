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