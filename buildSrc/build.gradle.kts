plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
}

dependencies {
    implementation(kotlin("gradle-plugin", "1.4.32"))
    implementation(kotlin("serialization", "1.4.32"))
    implementation("com.android.tools.build:gradle:7.0.0-alpha14")
}