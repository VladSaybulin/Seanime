package ru.vladsaybulin.feature.list.navigation

import ru.vladsaybulin.core.navigation.AbstractRouteWithArgumentsSerializer
import ru.vladsaybulin.core.navigation.RouteWithArguments

typealias ListScreenRoute = RouteWithArguments<ListArgs>

abstract class ListScreenRouteSerializer<Route : ListScreenRoute> :
    AbstractRouteWithArgumentsSerializer<ListArgs, Route>(ListArgs.serializer())