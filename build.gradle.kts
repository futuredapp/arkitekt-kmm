group = "app.futured.arkitekt"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(app.futured.arkitekt.Dependencies.Android.gradlePlugin)
        classpath(
            kotlin(
                app.futured.arkitekt.Dependencies.Kotlin.gradlePlugin,
                app.futured.arkitekt.Dependencies.Kotlin.version
            )
        )
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
