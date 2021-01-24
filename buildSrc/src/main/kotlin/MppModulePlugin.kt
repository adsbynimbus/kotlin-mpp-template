@file:Suppress("UnstableApiUsage")

import org.gradle.api.*
import org.gradle.api.credentials.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.tasks.Sync
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.*
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.lang.System.getenv

val Project.kotlinMpp: KotlinMultiplatformExtension
    get() = extensions.getByType(KotlinMultiplatformExtension::class)

inline fun Project.publishingExtension(crossinline block: PublishingExtension.() -> Unit) =
    extensions.configure(PublishingExtension::class) { block() }

class MppModulePlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        enableCompat()

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
                        implementation("junit:junit")
                    }
                }
                maybeCreate("iosMain")
                maybeCreate("iosTest")
            }
        }

        commonExtension {
            sourceSets.maybeCreate("main").apply {
                java.srcDirs("src/androidMain/kotlin")
                manifest.srcFile("src/androidMain/AndroidManifest.xml")
                res.srcDirs("src/androidMain/res")
            }
        }

        tasks.register<Sync>("packForXcode") {
            group = "build"
            val mode = getenv("CONFIGURATION") ?: "DEBUG"
            val framework = kotlinMpp.targets.getByName(
                iosTarget(getenv("SDK_NAME") ?: "iphonesimulator"),
                KotlinNativeTarget::class
            ).binaries.getFramework(mode)
            inputs.property("mode", mode)
            dependsOn(framework.linkTask)
            from({ framework.outputDirectory })
            into("$buildDir/xcode-frameworks")
        }.also {
            tasks.named("build").configure {
                dependsOn(it)
            }
        }

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

fun iosTarget(sdkName: String) = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"