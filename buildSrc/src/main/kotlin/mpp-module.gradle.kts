@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.lang.System.getenv

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `maven-publish`
}

android {
    compileSdk = project.compileSdk
    defaultConfig {
        minSdk = project.minSdk
        versionName = "${project.version}"
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    sourceSets {
        val main by getting {
            java.srcDirs("src/androidMain/kotlin")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res")
        }
    }
}

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
    ios {
        binaries.framework()
    }
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    val mode = getenv("CONFIGURATION") ?: "DEBUG"
    val sdkName = getenv("SDK_NAME") ?: "iphonesimulator"
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    from({ framework.outputDirectory })
    into("$buildDir/xcode-frameworks")
}
tasks.getByName("build").dependsOn(packForXcode)

getenv("GITHUB_REPOSITORY")?.let {
    publishing {
        repositories {
            maven {
                name = "github"
                url = uri("https://maven.pkg.github.com/$it")
                credentials(PasswordCredentials::class)
            }
        }
    }
}