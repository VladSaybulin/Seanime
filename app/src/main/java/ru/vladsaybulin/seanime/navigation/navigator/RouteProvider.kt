package ru.vladsaybulin.seanime.navigation.navigator

import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsArgs
import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsScreenRoute
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsArgs
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsScreenRoute
import ru.vladsaybulin.feature.details.navigation.TitleDetailsArgs
import ru.vladsaybulin.feature.details.navigation.TitleDetailsScreenRoute
import ru.vladsaybulin.feature.search.navigation.SearchArgs
import ru.vladsaybulin.feature.search.navigation.SearchScreenRoute
import ru.vladsaybulin.seanime.navigation.routes.CalendarGraph
import ru.vladsaybulin.seanime.navigation.routes.HomeGraph
import ru.vladsaybulin.seanime.navigation.routes.MyListGraph
import ru.vladsaybulin.seanime.navigation.routes.SearchGraph

class RouteProvider(
    val characterDetailsRoute: (CharacterDetailsArgs) -> CharacterDetailsScreenRoute,
    val searchScreenRoute: (SearchArgs) -> SearchScreenRoute,
    val titleDetailsScreenRoute: (TitleDetailsArgs) -> TitleDetailsScreenRoute,
    val titleAuthorsScreenRoute: (TitleAuthorsArgs) -> TitleAuthorsScreenRoute,
)

val HomeGraphRouteProvider = RouteProvider(
    HomeGraph::CharacterDetailsScreen,
    HomeGraph::SearchScreen,
    HomeGraph::TitleDetailsScreen,
    HomeGraph::TitleAuthorsScreen
)

val SearchGraphRouteProvider = RouteProvider(
    SearchGraph::CharacterDetailsScreen,
    SearchGraph::SearchScreen,
    SearchGraph::TitleDetailsScreen,
    SearchGraph::TitleAuthorsScreen
)

val MyListGraphRouteProvider = RouteProvider(
    MyListGraph::CharacterDetailsScreen,
    MyListGraph::SearchScreen,
    MyListGraph::TitleDetailsScreen,
    MyListGraph::TitleAuthorsScreen
)

val CalendarGraphRouteProvider = RouteProvider(
    CalendarGraph::CharacterDetailsScreen,
    CalendarGraph::SearchScreen,
    CalendarGraph::TitleDetailsScreen,
    CalendarGraph::TitleAuthorsScreen
)