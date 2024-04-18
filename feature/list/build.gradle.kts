plugins {
    alias(libs.plugins.shikimori.android.feature)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.shikimori.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.feature.list"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}