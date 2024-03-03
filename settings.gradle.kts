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

rootProject.name = "Shikimori"
include(":app")
include(":core:model")
include(":core:network-retrofit")
include(":core:database")
include(":core:common")
include(":core:network-common")
include(":core:datastore")
include(":core:data")
include(":core:designsystem")
include(":core:ui")
