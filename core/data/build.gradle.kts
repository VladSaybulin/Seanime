plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.shikimori.core.data"
}

dependencies {
    implementation(libs.kotlinx.datetime)

    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:model"))
    implementation(project(":core:network:common"))
    implementation(project(":core:network:retrofit"))
}