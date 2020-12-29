@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.extension.AndroidComponentsExtension
import com.android.build.api.variant.AndroidVersion
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.*
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.lang.System.getenv

fun Project.intProperty(propName: String) = Integer.parseInt(property(propName) as String)

val Project.compileSdk: Int get() = intProperty("android.sdk.compile")
val Project.minSdk: Int get() = intProperty("android.sdk.min")
val Project.targetSdk: Int get() = intProperty("android.sdk.target")

fun Project.androidx(library: String) =
    "androidx.$library:$library:${property("androidx.$library")}"

internal val Project.kotlinMpp: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class)

internal val Project.androidComponents: AndroidComponentsExtension<*, *>
    get() = extensions.getByType(AndroidComponentsExtension::class)

internal val Project.libraryExtension: LibraryExtension<*, *, *, *, *>
    get() = extensions.getByType(LibraryExtension::class)

internal val Project.publishingExtension: PublishingExtension
    get() = extensions.getByType(PublishingExtension::class)

class MppModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            enableCompat()

            androidComponents.apply {
                beforeVariants {
                    it.minSdkVersion = AndroidVersion(project.minSdk)
                    it.targetSdkVersion = AndroidVersion(project.targetSdk)
                }
            }

            libraryExtension.apply {
                compileSdk = project.compileSdk
                compileOptions {
                    sourceCompatibility(JavaVersion.VERSION_1_8)
                    targetCompatibility(JavaVersion.VERSION_1_8)
                }
                sourceSets.maybeCreate("main").apply {
                    java.srcDirs("src/androidMain/kotlin")
                    manifest.srcFile("src/androidMain/AndroidManifest.xml")
                    res.srcDirs("src/androidMain/res")
                }
            }

            kotlinMpp.apply {
                android {
                    publishLibraryVariants("release")
                    compilations.all {
                        kotlinOptions {
                            jvmTarget = JavaVersion.VERSION_1_8.toString()
                            useIR = true
                        }
                    }
                }
                ios {
                    binaries.framework()
                }
                sourceSets.apply {
                    maybeCreate("commonMain")
                    maybeCreate("commonTest").apply {
                        dependencies {
                            implementation(kotlin("test-common"))
                            implementation(kotlin("test-annotations-common"))
                        }
                    }
                    maybeCreate("androidMain")
                    maybeCreate("androidTest").apply {
                        dependencies {
                            implementation(kotlin("test-junit"))
                            implementation("junit:junit:4.13")
                        }
                    }
                    maybeCreate("iosMain")
                    maybeCreate("iosTest")
                }
            }

            project.tasks.register("packForXcode", Sync::class) {
                group = "build"
                val mode = getenv("CONFIGURATION") ?: "DEBUG"
                val framework = project.kotlinMpp.targets.getByName(
                    iosTarget(getenv("SDK_NAME") ?: "iphonesimulator"),
                    KotlinNativeTarget::class
                ).binaries.getFramework(mode)
                inputs.property("mode", mode)
                dependsOn(framework.linkTask)
                from({ framework.outputDirectory })
                into("${project.buildDir}/xcode-frameworks")
            }.also { project.tasks.named("build").dependsOn(it) }

            getenv("GITHUB_REPOSITORY")?.let {
                publishingExtension.repositories {
                    repositories {
                        maven {
                            name = "github"
                            url = uri("https://maven.pkg.github.com/$it")
                            credentials(PasswordCredentials::class)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Fixes a breaking conflict in AGP 7.0.0-alpha03 and Kotlin MPP
 */
fun Project.enableCompat() = also {
    configurations {
        maybeCreate("androidTestApi")
        maybeCreate("androidTestDebugApi")
        maybeCreate("androidTestReleaseApi")
        maybeCreate("testApi")
        maybeCreate("testDebugApi")
        maybeCreate("testReleaseApi")
    }
}

fun iosTarget(sdkName: String) = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"