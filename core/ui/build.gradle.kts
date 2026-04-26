plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.core.ui"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui2.strings)

    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.primeTransformer)
    implementation(libs.paging.compose)
    implementation(libs.zoomable)
}