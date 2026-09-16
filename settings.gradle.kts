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

pluginManagement {
    includeBuild("gradle/build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "Seanime"
include(":app")
include(":core:model")
include(":core:network")
include(":core:network-graphql")
include(":core:database")
include(":core:common")
include(":core:datastore")
include(":core:data")
include(":core:designsystem")
include(":core:ui")
include(":core:domain")
include(":core:auth")
include(":core:navigation")
include(":core:datastore-proto")
include(":core:textprocessor")
include(":feature:title:screenshots")
include(":feature:title:videos")
include(":core:ui2:strings")
include(":core:ui2:score")
include(":core:ui2:entry")

include(":feature:calendar:api")
include(":feature:calendar:impl")
include(":feature:character:api")
include(":feature:character:impl")
include(":feature:home:api")
include(":feature:home:impl")
include(":feature:imageview:api")
include(":feature:imageview:impl")
include(":feature:list:api")
include(":feature:list:impl")
include(":feature:profile:api")
include(":feature:profile:impl")
include(":feature:rate:editor:api")
include(":feature:rate:editor:impl")
include(":feature:search:api")
include(":feature:search:impl")
include(":feature:title:authors:api")
include(":feature:title:authors:impl")
include(":feature:title:characters:api")
include(":feature:title:characters:impl")
include(":feature:title:details:api")
include(":feature:title:details:impl")