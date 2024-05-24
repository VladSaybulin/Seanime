plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.shikimori.core.data"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:auth"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:network"))
    implementation(project(":core:textprocessor"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.paging.runtime)
}