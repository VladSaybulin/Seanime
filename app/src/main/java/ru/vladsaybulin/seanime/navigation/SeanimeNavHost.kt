package ru.vladsaybulin.seanime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import ru.vladsaybulin.feature.authors.navigation.titleAuthorsScreen
import ru.vladsaybulin.feature.calendar.navigation.calendarScreen
import ru.vladsaybulin.feature.character.navigation.characterDetailsScreen
import ru.vladsaybulin.feature.details.navigation.titleDetailsScreen
import ru.vladsaybulin.feature.home.navigation.homeScreen
import ru.vladsaybulin.feature.list.navigation.ListArgs
import ru.vladsaybulin.feature.list.navigation.listScreen
import ru.vladsaybulin.feature.search.navigation.SearchArgs
import ru.vladsaybulin.feature.search.navigation.searchScreen
import ru.vladsaybulin.seanime.navigation.navigator.HomeGraphRouteProvider
import ru.vladsaybulin.seanime.navigation.navigator.MyListGraphRouteProvider
import ru.vladsaybulin.seanime.navigation.navigator.SeanimeNavEvents
import ru.vladsaybulin.seanime.navigation.navigator.SeanimeNavEventsFactory
import ru.vladsaybulin.seanime.navigation.navigator.SearchGraphRouteProvider
import ru.vladsaybulin.seanime.navigation.navigator.toCalendarNavEvents
import ru.vladsaybulin.seanime.navigation.navigator.toCharacterDetailsNavEvents
import ru.vladsaybulin.seanime.navigation.navigator.toHomeNavEvents
import ru.vladsaybulin.seanime.navigation.navigator.toListNavHost
import ru.vladsaybulin.seanime.navigation.navigator.toSearchNavEvents
import ru.vladsaybulin.seanime.navigation.navigator.toTitleAuthorsNavEvents
import ru.vladsaybulin.seanime.navigation.navigator.toTitleDetailsNavEvents
import ru.vladsaybulin.seanime.navigation.routes.CalendarGraph
import ru.vladsaybulin.seanime.navigation.routes.HomeGraph
import ru.vladsaybulin.seanime.navigation.routes.MyListGraph
import ru.vladsaybulin.seanime.navigation.routes.SearchGraph

@Composable
fun SeanimeNavHost(
    navController: NavHostController,
    navEventsFactory: SeanimeNavEventsFactory,
    startDestination: TopLevelDestination = TopLevelDestination.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.graphRoute,
    ) {
        homeGraph(navEventsFactory.create(HomeGraphRouteProvider))
        searchGraph(navEventsFactory.create(SearchGraphRouteProvider))
        myListGraph(navEventsFactory.create(MyListGraphRouteProvider))
        calendarGraph(navEventsFactory.create(MyListGraphRouteProvider))
    }
}

fun NavGraphBuilder.homeGraph(navEvents: SeanimeNavEvents) {
    navigation<HomeGraph>(HomeGraph.HomeScreen) {
        characterDetailsScreen<HomeGraph.CharacterDetailsScreen>(navEvents.toCharacterDetailsNavEvents())
        homeScreen<HomeGraph.HomeScreen>(navEvents.toHomeNavEvents())
        titleDetailsScreen<HomeGraph.TitleDetailsScreen>(navEvents.toTitleDetailsNavEvents())
        titleAuthorsScreen<HomeGraph.TitleAuthorsScreen>(navEvents.toTitleAuthorsNavEvents())
        searchScreen<HomeGraph.SearchScreen>(navEvents.toSearchNavEvents())
    }
}

fun NavGraphBuilder.searchGraph(navEvents: SeanimeNavEvents) {
    navigation<SearchGraph>(SearchGraph.SearchScreen(SearchArgs.defaultSearch())) {
        characterDetailsScreen<SearchGraph.CharacterDetailsScreen>(navEvents.toCharacterDetailsNavEvents())
        titleDetailsScreen<SearchGraph.TitleDetailsScreen>(navEvents.toTitleDetailsNavEvents())
        titleAuthorsScreen<SearchGraph.TitleAuthorsScreen>(navEvents.toTitleAuthorsNavEvents())
        searchScreen<SearchGraph.SearchScreen>(navEvents.toSearchNavEvents())
    }
}

fun NavGraphBuilder.calendarGraph(navEvents: SeanimeNavEvents) {
    navigation<CalendarGraph>(CalendarGraph.CalendarScreen) {
        calendarScreen<CalendarGraph.CalendarScreen>(navEvents.toCalendarNavEvents())
        characterDetailsScreen<CalendarGraph.CharacterDetailsScreen>(navEvents.toCharacterDetailsNavEvents())
        titleDetailsScreen<CalendarGraph.TitleDetailsScreen>(navEvents.toTitleDetailsNavEvents())
        titleAuthorsScreen<CalendarGraph.TitleAuthorsScreen>(navEvents.toTitleAuthorsNavEvents())
        searchScreen<CalendarGraph.SearchScreen>(navEvents.toSearchNavEvents())
    }
}

fun NavGraphBuilder.myListGraph(navEvents: SeanimeNavEvents) {
    navigation<MyListGraph>(MyListGraph.ListScreen(ListArgs())) {
        listScreen<MyListGraph.ListScreen>(navEvents.toListNavHost())
        characterDetailsScreen<CalendarGraph.CharacterDetailsScreen>(navEvents.toCharacterDetailsNavEvents())
        titleDetailsScreen<CalendarGraph.TitleDetailsScreen>(navEvents.toTitleDetailsNavEvents())
        titleAuthorsScreen<CalendarGraph.TitleAuthorsScreen>(navEvents.toTitleAuthorsNavEvents())
        searchScreen<CalendarGraph.SearchScreen>(navEvents.toSearchNavEvents())
    }
}