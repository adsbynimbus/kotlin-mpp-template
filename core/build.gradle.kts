plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    `maven-publish`
}

enableCompat()

group = defaultGroup
version = defaultVersion

kotlin {
    android {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "${Jvm.target}"
                useIR = true
            }
        }
    }
    ios()
    cocoapods {
        summary = "A Kotlin MPP Cocoapods Template Library"
        homepage = GithubRepo.path
        ios.deploymentTarget = Ios.deployTarget
        podfile = rootProject.file(Ios.podfile)
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.bundles.mpp.core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.bundles.mpp.android)
                api(libs.bundles.android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val iosMain by getting {
            dependencies {
                api(libs.bundles.mpp.ios)
            }
        }
    }
}

android {
    compileSdk = Android.compileSdk
    defaultConfig.minSdk = Android.minSdk
    val main by sourceSets.getting {
        java.srcDirs("src/androidMain/kotlin")
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
    }
}

publishing {
    repositories.maven {
        name = "github"
        url = project.uri(GithubRepo.packages)
        credentials(PasswordCredentials::class)
    }.takeIf { GithubRepo.name != null }
}

val cleanPodBuild by tasks.registering {
    arrayOf(buildDir, file("$name.podspec"), file("gen"), file("Pods")).let {
        destroyables.register(it)
        delete(it)
    }
}
val clean by tasks.getting {
    dependsOn(cleanPodBuild)
}