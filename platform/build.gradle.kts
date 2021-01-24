plugins {
    `java-platform`
}

dependencies {
    constraints {
        api("androidx.annotation:annotation:1.1.0")
        api("androidx.appcompat:appcompat:1.2.0")
        api("androidx.constraintlayout:constraintlayout:2.0.4")
        api("com.android.tools.build:gradle:4.1.2")
        api("junit:junit:4.13")
        api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.21")
        api("org.jetbrains.kotlinx:kotlinx-coroutines:1.4.2")
    }
}