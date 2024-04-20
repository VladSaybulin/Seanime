plugins {
    alias(libs.plugins.shikimori.android.library)
}

android {
    namespace = "ru.vladsaybulin.core.navigation"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.navigation.ui.ktx)
}