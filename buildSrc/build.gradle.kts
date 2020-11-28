plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    gradlePluginPortal()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:${property("agp")}")
    implementation(kotlin("gradle-plugin", "${property("kgp")}"))
}