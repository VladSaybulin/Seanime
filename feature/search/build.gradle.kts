plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.search"
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.core.data)
    implementation(projects.core.ui2.entry)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    implementation(libs.kotlinx.serialization.json)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.androidx.navigation.testing)
}