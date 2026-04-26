plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.userrate"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.ui2.strings)
}