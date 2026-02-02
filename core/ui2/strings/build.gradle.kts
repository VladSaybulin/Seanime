plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.core.ui2.strings"
}

dependencies {
    implementation(projects.core.model)

    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
}