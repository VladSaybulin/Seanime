package ru.vladsaybulin.feature.search.navigation

import ru.vladsaybulin.core.navigation.AbstractRouteWithArgumentsSerializer
import ru.vladsaybulin.core.navigation.RouteWithArguments

typealias SearchScreenRoute = RouteWithArguments<SearchArgs>

abstract class SearchScreenRouteSerializer<Route : SearchScreenRoute> :
    AbstractRouteWithArgumentsSerializer<SearchArgs, Route>(SearchArgs.serializer())