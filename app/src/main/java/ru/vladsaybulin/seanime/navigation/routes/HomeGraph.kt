package ru.vladsaybulin.seanime.navigation.routes

import kotlinx.serialization.Serializable
import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsArgs
import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsScreenRoute
import ru.vladsaybulin.feature.authors.navigation.TitleAuthorsScreenRouteSerializer
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsArgs
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsScreenRoute
import ru.vladsaybulin.feature.character.navigation.CharacterDetailsScreenRouteSerializer
import ru.vladsaybulin.feature.details.navigation.TitleDetailsArgs
import ru.vladsaybulin.feature.details.navigation.TitleDetailsScreenRoute
import ru.vladsaybulin.feature.details.navigation.TitleDetailsScreenRouteSerializer
import ru.vladsaybulin.feature.search.navigation.SearchArgs
import ru.vladsaybulin.feature.search.navigation.SearchScreenRoute
import ru.vladsaybulin.feature.search.navigation.SearchScreenRouteSerializer

@Serializable
object HomeGraph {

    @Serializable
    object HomeScreen

    @Serializable(SearchScreen.Serializer::class)
    data class SearchScreen(override val args: SearchArgs) : SearchScreenRoute {
        companion object Serializer : SearchScreenRouteSerializer<SearchScreen>()
    }

    @Serializable(TitleDetailsScreen.Serializer::class)
    data class TitleDetailsScreen(override val args: TitleDetailsArgs) : TitleDetailsScreenRoute {
        companion object Serializer : TitleDetailsScreenRouteSerializer<TitleDetailsScreen>()
    }

    @Serializable(TitleAuthorsScreen.SerializerTitle::class)
    data class TitleAuthorsScreen(override val args: TitleAuthorsArgs) : TitleAuthorsScreenRoute {
        companion object SerializerTitle : TitleAuthorsScreenRouteSerializer<TitleAuthorsScreen>()
    }

    @Serializable(CharacterDetailsScreen.Serializer::class)
    data class CharacterDetailsScreen(override val args: CharacterDetailsArgs) :
        CharacterDetailsScreenRoute {
        companion object Serializer : CharacterDetailsScreenRouteSerializer<CharacterDetailsScreen>()
    }
}