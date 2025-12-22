plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.core.domain"
}

dependencies {
    implementation(projects.core.auth)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.common)

    implementation(libs.kotlinx.datetime)
    implementation(libs.paging.runtime)
}