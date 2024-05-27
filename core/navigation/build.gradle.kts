plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.core.navigation"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.kotlinx.serialization.json)
}