plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.character"
}

dependencies {
    implementation(projects.core.ui2.entry)
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(libs.coil.kt.compose)
}