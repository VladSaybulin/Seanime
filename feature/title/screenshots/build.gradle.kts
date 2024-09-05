plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.title.screenshots"
}

dependencies {
    implementation(project(":core:data"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.kt.compose)
}