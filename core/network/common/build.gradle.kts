plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.network.common"
}

dependencies {
    api(libs.okhttp)
    implementation(libs.okhttp.logging)
}