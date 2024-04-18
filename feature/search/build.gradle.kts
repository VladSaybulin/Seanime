plugins {
    alias(libs.plugins.shikimori.android.feature)
    alias(libs.plugins.shikimori.android.library.compose)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.search"
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:data"))

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}