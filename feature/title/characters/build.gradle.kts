plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.title.characters"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui2.entry)
    implementation(libs.kotlinx.serialization.json)
}