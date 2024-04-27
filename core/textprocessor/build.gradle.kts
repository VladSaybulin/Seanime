plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.core.textprocessor"
}

dependencies {
    implementation(libs.primeTransformer)
}