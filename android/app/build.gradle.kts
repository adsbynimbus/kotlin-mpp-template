plugins {
    id("com.android.application")
    kotlin("android")
}

apply<AndroidModulePlugin>()

android {
    defaultConfig {
        applicationId("${project.group}.android")
        targetSdkVersion(project.targetSdk)
        versionCode(1)
        versionName("${project.version}")
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        useIR = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(androidx("annotation"))
    implementation(androidx("appcompat"))
    implementation(androidx("constraintlayout"))
}