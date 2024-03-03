plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.core.ui"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
}