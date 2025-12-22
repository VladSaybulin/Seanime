plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
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
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.model)
    api(libs.appauth)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}