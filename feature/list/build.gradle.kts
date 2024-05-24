plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.seanime.android.library.compose)
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