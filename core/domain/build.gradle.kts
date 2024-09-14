plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.core.domain"
}

dependencies {
    implementation(project(":core:auth"))
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    implementation(project(":core:common"))

    implementation(libs.kotlinx.datetime)
    implementation(libs.paging.runtime)
}