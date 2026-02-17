plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.home"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.navigation)
    implementation(projects.core.ui2.entry)
    implementation(projects.feature.userrate)


    implementation(libs.coil.kt.compose)
    implementation(libs.primeTransformer)

    implementation(libs.kotlinx.serialization.json)
}