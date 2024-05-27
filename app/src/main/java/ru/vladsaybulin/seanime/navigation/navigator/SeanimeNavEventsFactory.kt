package ru.vladsaybulin.seanime.navigation.navigator

import androidx.navigation.NavController
import ru.vladsaybulin.model.common.Image

class SeanimeNavEventsFactory(
    private val navController: NavController,
    private val openUrl: (String) -> Unit,
    private val onAuth: () -> Unit,
    private val openUserRateEditor: () -> Unit,
    private val openFullscreenImage: (images: List<Image>, startImageIndex: Int) -> Unit
) {
    fun create(routeProvider: RouteProvider) = SeanimeNavEventsImpl(
        navController = navController,
        openUrl = openUrl,
        onAuth = onAuth,
        openUserRateEditor = openUserRateEditor,
        openFullscreenImage = openFullscreenImage,
        provider = routeProvider
    )
}