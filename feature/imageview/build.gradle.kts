plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.imageview"
}

dependencies {
    implementation(projects.core.navigation)

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.zoomable)
    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.navigation.compose)
}
