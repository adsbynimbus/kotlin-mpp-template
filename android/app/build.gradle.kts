plugins {
    id("com.android.application")
    kotlin("android")
}

group = defaultGroup
version = defaultVersion

android {
    compileSdkVersion(Android.compileSdk)
    defaultConfig {
        applicationId("$group.android")
        minSdkVersion(Android.minSdk)
        targetSdkVersion(Android.targetSdk)
        versionCode(1)
        versionName("$version")
    }

    buildFeatures {
        viewBinding = true
    }

    val release by buildTypes.getting {
        isMinifyEnabled = false
    }

    kotlinOptions {
        jvmTarget = "${Jvm.target}"
        useIR = true
    }
}

dependencies {
    implementation(project(":core"))
}