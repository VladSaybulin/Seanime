plugins {
    alias(libs.plugins.shikimori.android.feature)
    alias(libs.plugins.shikimori.android.library.compose)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.imageview"
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.zoomable)
    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.navigation.compose)
}
