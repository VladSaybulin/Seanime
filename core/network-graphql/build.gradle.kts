plugins {
    alias(libs.plugins.seanime.android.library)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.apollo.graphql)
}

android {
    namespace = "ru.vladsaybulin.core.network.graphql"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.apollo.graphql.adapters)
    api(libs.apollo.graphql.runtime)
}

apollo {
    service("service") {
        addTypename = "ifAbstract"
        packageName.set("ru.vladsaybulin.core.network.graphql")
        mapScalarToKotlinLong("ID")
        mapScalar(
            graphQLName = "ISO8601DateTime",
            targetName = "kotlinx.datetime.Instant",
            expression = "com.apollographql.apollo3.adapter.KotlinxInstantAdapter"
        )
        mapScalar(
            graphQLName = "ISO8601Date",
            targetName = "kotlinx.datetime.LocalDate",
            expression = "com.apollographql.apollo3.adapter.KotlinxLocalDateAdapter"
        )
        mapScalarToKotlinInt("PositiveInt")
    }
}