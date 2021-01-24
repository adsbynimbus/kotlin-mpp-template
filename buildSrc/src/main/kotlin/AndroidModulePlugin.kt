@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.*
import org.gradle.kotlin.dsl.configure

fun Project.intProperty(propName: String) = Integer.parseInt(property(propName) as String)

val Project.compileSdk: Int get() = intProperty("android.sdk.compile")
val Project.minSdk: Int get() = intProperty("android.sdk.min")
val Project.targetSdk: Int get() = intProperty("android.sdk.target")

fun androidx(library: String) = "androidx.$library:$library"

class AndroidModulePlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.androidCommon {
            compileSdk = project.compileSdk
            compileOptions {
                sourceCompatibility(JavaVersion.VERSION_1_8)
                targetCompatibility(JavaVersion.VERSION_1_8)
            }
            defaultConfig.minSdk = project.minSdk
        }
    }
}

/* Helper methods to enable Kotlin script like syntax */

inline fun Project.androidCommon(crossinline block: CommonExtension<*,*,*,*,*,*,*,*>.() -> Unit) =
    extensions.configure(CommonExtension::class) { block() }