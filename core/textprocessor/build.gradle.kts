plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.core.textprocessor"
}

dependencies {
    implementation(libs.primeTransformer)
    implementation(libs.kotlinx.serialization.json)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
}