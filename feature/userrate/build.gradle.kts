plugins {
    alias(libs.plugins.shikimori.android.feature)
    alias(libs.plugins.shikimori.android.library.compose)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.userrate"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:domain"))
}