import java.util.Properties

plugins {
    alias(libs.plugins.shikimori.android.application)
    alias(libs.plugins.shikimori.android.application.compose)
    alias(libs.plugins.shikimori.android.hilt)
}

android {
    namespace = "ru.vladsaybulin.shikimori"

    defaultConfig {
        applicationId = "ru.vladsaybulin.shikimori"
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }

        val secretsProperties = Properties().apply {
            file("../secrets.properties").inputStream().use { fis ->
                load(fis)
            }
        }
        val redirectUrl = requireNotNull(secretsProperties["SHIKIMORI_AUTH_REDIRECT_URI"]).toString()
        val redirectScheme = redirectUrl.substringBefore(':')
        android.defaultConfig.manifestPlaceholders["appAuthRedirectScheme"] = redirectScheme
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(project(":core:auth"))
    implementation(project(":core:data"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:common"))
    implementation(project(":core:navigation"))
    implementation(project(":core:model"))


    implementation(project(":feature:calendar"))
    implementation(project(":feature:details"))
    implementation(project(":feature:home"))
    implementation(project(":feature:list"))
    implementation(project(":feature:imageview"))
    implementation(project(":feature:search"))
    implementation(project(":feature:userrate"))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.hilt.navigation.compose)

}