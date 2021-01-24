plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    `maven-publish`
}

apply<AndroidModulePlugin>()
apply<MppModulePlugin>()

dependencies {
    api(platform(project(":platform")))
}