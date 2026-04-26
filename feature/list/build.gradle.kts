plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.seanime.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.feature.list"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.core.ui2.entry)
    implementation(projects.core.ui2.strings)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
}