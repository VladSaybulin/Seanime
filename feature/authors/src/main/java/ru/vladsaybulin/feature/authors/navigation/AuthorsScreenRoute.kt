package ru.vladsaybulin.feature.authors.navigation

import ru.vladsaybulin.core.navigation.AbstractRouteWithArgumentsSerializer
import ru.vladsaybulin.core.navigation.RouteWithArguments

typealias TitleAuthorsScreenRoute = RouteWithArguments<TitleAuthorsArgs>

abstract class TitleAuthorsScreenRouteSerializer <Route : RouteWithArguments<TitleAuthorsArgs>> :
    AbstractRouteWithArgumentsSerializer<TitleAuthorsArgs, Route>(TitleAuthorsArgs.serializer()) {
}