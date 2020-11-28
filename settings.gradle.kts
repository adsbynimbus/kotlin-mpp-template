pluginManagement {
    val agp: String by settings
    val kgp: String by settings
    resolutionStrategy {
        eachPlugin {
            when(requested.id.namespace) {
                "com.android" -> useModule("com.android.tools.build:gradle:$agp")
                "org.jetbrains.kotlin" -> useVersion(kgp)
            }
        }
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        gradlePluginPortal()
        jcenter()
        mavenCentral()
    }
}


rootProject.name = "kotlin-mpp-template"

include(":android:app")
include(":core")
