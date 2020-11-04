pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
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

rootProject.name = "kotlin-mpp-template"

include(":android:app")
include(":core")
