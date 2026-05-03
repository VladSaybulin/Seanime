/*
 * Copyright 2026 Vlad Saybulin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Properties

plugins {
    alias(libs.plugins.seanime.android.application)
    alias(libs.plugins.seanime.android.application.compose)
    alias(libs.plugins.seanime.android.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.kotlinx.serialization)
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "ru.vladsaybulin.seanime"

    defaultConfig {
        applicationId = "ru.vladsaybulin.seanime"
        versionCode = 1
        versionName = "0.0.1"

        vectorDrawables {
            useSupportLibrary = true
        }

        val secretsProperties = Properties().apply {
            file("../secrets.default.properties").inputStream().use { fis ->
                load(fis)
            }
        }
        val redirectUrl = requireNotNull(secretsProperties["SHIKIMORI_AUTH_REDIRECT_URI"]).toString()
        val redirectScheme = redirectUrl.substringBefore(':')
        android.defaultConfig.manifestPlaceholders["appAuthRedirectScheme"] = redirectScheme
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            resValue("string", "app_name", "Seanime [dev]")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
            signingConfig = buildTypes.getAt("debug").signingConfig
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(projects.core.auth)
    implementation(projects.core.data)
    implementation(projects.core.designsystem)
    implementation(projects.core.common)
    implementation(projects.core.navigation)
    implementation(projects.core.model)
    implementation(projects.core.ui)
    implementation(projects.feature.calendar)
    implementation(projects.feature.home)
    implementation(projects.feature.list)
    implementation(projects.feature.imageview)
    implementation(projects.feature.search)
    implementation(projects.feature.userrate)
    implementation(projects.feature.character)
    implementation(projects.feature.title.authors)
    implementation(projects.feature.title.details)
    implementation(projects.feature.title.related)
    implementation(projects.feature.title.characters)
    implementation(projects.feature.title.screenshots)
    implementation(projects.feature.title.videos)
    implementation(projects.feature.profile)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.kt)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.google.oss.licenses)

    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.analytics)
    implementation(libs.google.firebase.crashlytics)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.retrofit.core)

}
