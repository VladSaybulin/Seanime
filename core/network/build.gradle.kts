plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
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

    api(projects.core.networkGraphql)

    implementation(projects.core.auth)
    implementation(projects.core.model)
    implementation(projects.core.common)

    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.retrofit.core)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)
}