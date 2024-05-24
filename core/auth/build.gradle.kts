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
    implementation(project(":core:common"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    api(libs.appauth)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
}