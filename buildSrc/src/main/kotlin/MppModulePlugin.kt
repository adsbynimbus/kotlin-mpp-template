@file:Suppress("UnstableApiUsage")

import org.gradle.api.*
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.cocoapods.*
import java.lang.System.getenv

val Project.xcodeproj get() = property("xcodeproj") as String

class MppModulePlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {
        enableCompat()

        kotlin {
            android {
                publishLibraryVariants("release")
                compilations.all {
                    kotlinOptions {
                        jvmTarget = JavaVersion.VERSION_1_8.toString()
                        useIR = true
                    }
                }
            }
            ios()
            cocoapods {
                summary = "A Kotlin MPP Cocoapods Template Library"
                homepage = "https://www.github.com/${getenv("GITHUB_REPOSITORY")}"

                podfile = rootProject.file("$xcodeproj/Podfile")

                ios.deploymentTarget = "13.5"
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
                        implementation("junit:junit")
                    }
                }
                maybeCreate("iosMain")
                maybeCreate("iosTest")
            }
        }

        androidCommon {
            sourceSets.maybeCreate("main").apply {
                java.srcDirs("src/androidMain/kotlin")
                manifest.srcFile("src/androidMain/AndroidManifest.xml")
                res.srcDirs("src/androidMain/res")
            }
        }

        tasks.register<Delete>("cleanPodBuild") {
            arrayOf(buildDir, file("$name.podspec"), file("gen"), file("Pods")).let {
                destroyables.register(it)
                delete(it)
            }
        }.also { tasks.named("clean").configure { dependsOn(it) } }

        configurePublishing()
    }
}

fun Project.configurePublishing() {
    getenv("GITHUB_REPOSITORY")?.let {
        publishingExtension {
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

/**
 * Fixes a breaking conflict in AGP 7.0.0-alpha03 and Kotlin MPP
 */
fun Project.enableCompat() {
    configurations {
        maybeCreate("androidTestApi")
        maybeCreate("androidTestDebugApi")
        maybeCreate("androidTestReleaseApi")
        maybeCreate("testApi")
        maybeCreate("testDebugApi")
        maybeCreate("testReleaseApi")
    }
}

/* Helper methods to enable Kotlin script like syntax */

inline fun KotlinMultiplatformExtension.cocoapods(crossinline block: CocoapodsExtension.() -> Unit) =
    (this as ExtensionAware).extensions.configure(CocoapodsExtension::class) { block() }

inline fun Project.kotlin(crossinline block: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure(KotlinMultiplatformExtension::class) { block() }

inline fun Project.publishingExtension(crossinline block: PublishingExtension.() -> Unit) =
    extensions.configure(PublishingExtension::class) { block() }