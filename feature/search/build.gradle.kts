plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
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