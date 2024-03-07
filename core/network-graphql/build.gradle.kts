plugins {
    alias(libs.plugins.shikimori.android.library)
    alias(libs.plugins.shikimori.android.hilt)
    alias(libs.plugins.apollo.graphql)
}

android {
    namespace = "ru.vladsaybulin.core.network.graphql"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    api(libs.apollo.graphql.runtime)
}

apollo {
    service("service") {
        packageName.set("ru.vladsaybulin.core.network.graphql")
    }
}