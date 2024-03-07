plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.vladsaybulin.network.common"

    buildFeatures {
        buildConfig = true
    }
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.default.properties"
}

dependencies {
    api(project(":core:network-graphql"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}