plugins {
    id("com.android.library")
    kotlin("multiplatform")
    `maven-publish`
}

apply<AndroidModulePlugin>()
apply<MppModulePlugin>()