plugins {
    alias(libs.plugins.shikimori.android.feature)
    alias(libs.plugins.shikimori.android.library.compose)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.details"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(libs.primeTransformer)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt.compose)

    implementation(libs.kotlinx.serialization.json)
}