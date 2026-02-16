plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.core.ui2.entry"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    api(projects.core.ui2.strings)
    implementation(projects.core.ui2.score)

    implementation(libs.coil.kt.compose)
}