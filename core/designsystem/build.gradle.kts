plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.core.designsystem"
}

dependencies {
    implementation(projects.core.model)

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.ui.util)
    debugApi(libs.androidx.compose.ui.tooling)
}