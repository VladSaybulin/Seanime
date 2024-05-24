plugins {
    alias(libs.plugins.seanime.android.feature)
    alias(libs.plugins.seanime.android.library.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "ru.vladsaybulin.feature.details"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:navigation"))

    implementation(libs.primeTransformer)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil.kt.compose)

    implementation(libs.androidx.activity.compose)

    implementation(libs.kotlinx.serialization.json)
}