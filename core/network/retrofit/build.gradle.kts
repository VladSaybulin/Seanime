plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.network.retrofit"
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:network:common"))

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.datetime)
}