@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.extension.AndroidComponentsExtension
import com.android.build.api.variant.AndroidVersion
import org.gradle.api.*
import org.gradle.kotlin.dsl.getByType

fun Project.intProperty(propName: String) = Integer.parseInt(property(propName) as String)

val Project.compileSdk: Int get() = intProperty("android.sdk.compile")
val Project.minSdk: Int get() = intProperty("android.sdk.min")
val Project.targetSdk: Int get() = intProperty("android.sdk.target")

fun Project.androidx(library: String) =
    "androidx.$library:$library:${property("androidx.$library")}"

internal val Project.androidComponents: AndroidComponentsExtension<*, *>
    get() = extensions.getByType(AndroidComponentsExtension::class)

internal val Project.commonExtension: CommonExtension<*,*,*,*,*,*,*,*>
    get() = extensions.getByType(CommonExtension::class)

class AndroidModulePlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.androidComponents.apply {
            beforeVariants {
                it.minSdkVersion = AndroidVersion(project.minSdk)
                it.targetSdkVersion = AndroidVersion(project.targetSdk)
            }
        }

        project.commonExtension.apply {
            compileSdk = project.compileSdk
            compileOptions {
                sourceCompatibility(JavaVersion.VERSION_1_8)
                targetCompatibility(JavaVersion.VERSION_1_8)
            }
        }
    }
}