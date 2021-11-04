# Installation

## Setup project

Arkitekt-kmm is a kotlin multiplatform mobile library, so you have to use it in KMM project.
First add sonatype repository to your project `build.gradle.kts` as this library is available as snapshot.
```kotlin
// project root build.gradle.kts
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(...)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // sonatype repository for snapshots
        maven { setUrl("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}
```
Second add the dependency to your `shared/build.gradle.kts` as a `commonMain` dependency. We recommend
to add it as `api` configuration instead of `implementation` as it will expose the library for Android app.
We recommend to add `km-viewmodel` to `iosMain` as well as it make usage on iOS easier.

## Setup shared module
```kotlin
// shared module build.gradle.kts
kotlin {
    ios()
    android()
    sourceSets {
        val commonMain by getting {
            dependencies {
                // Arkitekt usecases
                api("app.futured.arkitekt:km-usecases:0.1.1-SNAPSHOT")
            }
        }
        val androidMain by getting {
            dependencies {
                ...
            }
        }
        val iosMain by getting {
            dependencies {
                api("app.futured.arkitekt:km-viewmodel:0.1.2-SNAPSHOT")
            }
        }
    }
}
```

## Usage

Now you are all set up, head to the [Usage](../UseCases/UseCase.md) section.
