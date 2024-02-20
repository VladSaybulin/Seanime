plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.protobuf)
    alias(libs.plugins.kapt)
    alias(libs.plugins.dagger.hilt)
}

android {
    namespace = "ru.vladsaybulin.datastore"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.core.ktx)

    implementation(libs.datastore)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(project(":core:common"))
    implementation(project(":core:model"))
}

protobuf {
    protoc {
        artifact = libs.protobuf.protoc.get().toString()
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                register("java") {
                    option("lite")
                }
                register("kotlin") {
                    option("lite")
                }
            }
        }
    }
}