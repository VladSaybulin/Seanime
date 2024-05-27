package ru.vladsaybulin.feature.details.navigation

import ru.vladsaybulin.core.navigation.AbstractRouteWithArgumentsSerializer
import ru.vladsaybulin.core.navigation.RouteWithArguments

typealias TitleDetailsScreenRoute = RouteWithArguments<TitleDetailsArgs>

abstract class TitleDetailsScreenRouteSerializer<Route : TitleDetailsScreenRoute> :
        AbstractRouteWithArgumentsSerializer<TitleDetailsArgs, Route>(TitleDetailsArgs.serializer())