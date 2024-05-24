plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.character"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(libs.coil.kt.compose)
}