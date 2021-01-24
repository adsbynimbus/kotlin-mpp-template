plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `maven-publish`
}

apply<AndroidModulePlugin>()
apply<MppModulePlugin>()

dependencies {
    api(platform(project(":platform")))
}