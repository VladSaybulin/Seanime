plugins {
    alias(libs.plugins.shikimori.android.feature)
    alias(libs.plugins.shikimori.android.library.compose)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.feature.home"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:navigation"))
    implementation(project(":feature:userrate"))


    implementation(libs.coil.kt.compose)
    implementation(libs.primeTransformer)
}