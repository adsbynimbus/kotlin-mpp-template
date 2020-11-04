# Kotlin Multiplatform Client ![Core](https://github.com/timehop/kotlin-mpp-template/workflows/Core/badge.svg)

This repository is a template for creating a new Kotlin Mulitplatform project with Android and iOS
targets using the latest build tooling.

## IDE Setup

Before you begin working with the project, install the Kotlin Multiplatform plugin for your IDE of 
choice.

[Android Studio](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)
[XCode](https://github.com/touchlab/xcode-kotlin)

## Versioning

### Project

The project version defaults to `development` unless built within Github actions which will compute
the version using tag ref that started the build.

### Dependencies

Versions for all dependencies are defined in the gradle.properties file to centralize where updates
to libraries happen. 

## One Click Deployments

To publish a version of the client library, push a tag to the default branch or use the Releases
page to create a new release. This will publish a version that matches the tag name with a 
package name generated when deploying from Github Actions in the format `${owner}.mpp` 

## Integrating with an existing project

Add the following to your Android build

```kotlin
repositories {
    maven {
        name = "github"
        url = uri("https://maven.pkg.github.com/${owner}/${repository}")
        credentials(PasswordCredentials)
        // Optional filter to speed up dependency resolution
        content {
            includeGroup("${owner}.mpp")
        }
    }
}

dependencies {
    implementation("${owner}.mpp:core")
}
```

### buildSrc gradle.properties symlink

To simplify updates to gradle.properties, the buildSrc gradle.properties file is symlinked to the
root of the project.

#### Windows symlinks

Symlink creation requires admin access. If you have admin access delete the 
buildSrc/gradle.properties and run the following command from the root of the project

```shell script
cmd> mklink /H "buildSrc/gradle.properties" "gradle.properties"
```