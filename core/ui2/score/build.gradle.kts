plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.library.compose)
}

android {
    namespace = "ru.vladsaybulin.core.ui2.score"
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.ui2.strings)

    implementation(libs.kotlinx.datetime)
}