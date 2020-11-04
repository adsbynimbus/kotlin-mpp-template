plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

repositories {
    jcenter()
    google()
}

dependencies {
    implementation("com.android.tools.build:gradle:${property("agp")}")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kgp")}")

}