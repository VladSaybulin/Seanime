package ru.vladsaybulin.seanime

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SeanimeApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .crossfade(true)
            .build()
}