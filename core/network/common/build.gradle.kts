plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.network.common"
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}