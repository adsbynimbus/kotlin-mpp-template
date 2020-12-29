plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
}

dependencies {
    implementation(kotlin("gradle-plugin"))
    implementation("com.android.tools.build:gradle-api:${property("agp")}")
    implementation("com.android.tools.build:gradle:${property("agp")}")
}