plugins {
    alias(libs.plugins.seanime.android.library)
}

android {
    namespace = "ru.vladsaybulin.core.navigation"
}

dependencies {
    implementation(project(":core:model"))
    implementation(libs.androidx.navigation.ui.ktx)
}