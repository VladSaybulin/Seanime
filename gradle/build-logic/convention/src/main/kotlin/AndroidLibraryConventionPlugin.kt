import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import ru.vladsaybulin.seanime.configureKotlinAndroid

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34

                //A stub for the AppAuth-Android library. Required to build a specific module
                defaultConfig.manifestPlaceholders["appAuthRedirectScheme"] = "stub"
            }
        }
    }
}