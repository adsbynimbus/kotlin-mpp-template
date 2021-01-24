plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
}

dependencies {
    api(platform(project(":platform")))
    implementation(kotlin("gradle-plugin"))
    implementation("com.android.tools.build:gradle")
}