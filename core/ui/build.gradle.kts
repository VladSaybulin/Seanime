plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.library.compose)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.core.ui"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.primeTransformer)

    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}