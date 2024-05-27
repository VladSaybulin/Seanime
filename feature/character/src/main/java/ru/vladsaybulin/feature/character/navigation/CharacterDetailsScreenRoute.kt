package ru.vladsaybulin.feature.character.navigation

import ru.vladsaybulin.core.navigation.AbstractRouteWithArgumentsSerializer
import ru.vladsaybulin.core.navigation.RouteWithArguments

typealias CharacterDetailsScreenRoute = RouteWithArguments<CharacterDetailsArgs>

abstract class CharacterDetailsScreenRouteSerializer<Route : CharacterDetailsScreenRoute> :
    AbstractRouteWithArgumentsSerializer<CharacterDetailsArgs, Route>(
        CharacterDetailsArgs.serializer()
    )