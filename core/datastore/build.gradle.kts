plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.datastore"
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:datastore-proto"))

    implementation(libs.androidx.dataStore.core)
    implementation(libs.kotlinx.datetime)
}