plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.secrets)
}

android {
    namespace = "ru.vladsaybulin.core.auth"

    buildFeatures {
        buildConfig = true
    }

    secrets {
        propertiesFileName = "secrets.properties"
        defaultPropertiesFileName = "secrets.default.properties"
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    api(libs.appauth)
}