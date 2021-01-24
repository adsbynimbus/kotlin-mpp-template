@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        google()
        jcenter()
    }
}


rootProject.name = "kotlin-mpp-template"

include(":android:app")
include(":core")
include(":platform")
