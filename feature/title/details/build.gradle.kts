plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.title.details"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.core.ui2.strings)

    implementation(libs.primeTransformer)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt.compose)

    implementation(libs.androidx.activity.compose)

    implementation(libs.kotlinx.serialization.json)
}