@file:Suppress("UnstableApiUsage")

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlin-mpp-template"

include("core", "android:app")
