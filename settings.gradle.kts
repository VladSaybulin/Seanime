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
include(":feature:calendar")
include(":core:domain")
include(":feature:imageview")
include(":feature:userrate")
include(":core:auth")
include(":feature:home")
include(":feature:list")
include(":core:navigation")
include(":feature:search")
include(":core:datastore-proto")
include(":feature:character")
include(":core:textprocessor")
include(":feature:title:authors")
include(":feature:title:related")
include(":feature:title:details")
include(":feature:title:characters")
include(":feature:title:screenshots")
include(":feature:title:videos")
include(":feature:profile")
include(":core:ui2:strings")
include(":core:ui2:score")
include(":core:ui2:entry")