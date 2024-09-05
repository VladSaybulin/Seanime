plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.titlerelated"
}

dependencies {
    implementation(project(":core:data"))
    implementation(libs.kotlinx.serialization.json)
}