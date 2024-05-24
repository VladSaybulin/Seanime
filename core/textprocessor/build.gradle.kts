plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.core.textprocessor"
}

dependencies {
    implementation(project(":core:model"))

    api(libs.primeTransformer)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
}