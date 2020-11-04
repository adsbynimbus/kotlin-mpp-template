plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(project(":core"))
    implementation(androidx("annotation"))
    implementation(androidx("appcompat"))
    implementation(androidx("constraintlayout"))
}

android {
    compileSdk = project.compileSdk
    defaultConfig {
        applicationId = "${project.group}.android"
        minSdk = project.minSdk
        targetSdk = project.targetSdk
        versionCode = 1
        versionName = "${project.version}"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        useIR = true
    }
}