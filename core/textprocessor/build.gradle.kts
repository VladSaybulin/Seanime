plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.core.textprocessor"
}

dependencies {
    implementation(project(":core:model"))

    implementation(libs.primeTransformer)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}