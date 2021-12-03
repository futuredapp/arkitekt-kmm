package app.futured.arkitekt

object Dependencies {
    object Kotlin {
        const val version = "1.6.0"

        const val gradlePlugin = "gradle-plugin"
    }

    object KotlinX {
        const val version = "1.6.0-RC"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Android {
        private const val agpVersion = "7.0.0"

        const val gradlePlugin = "com.android.tools.build:gradle:$agpVersion"
    }

    object MavenPublish {
        private const val version = "0.18.0"
        const val plugin = "com.vanniktech:gradle-maven-publish-plugin:$version"
    }

    object Dokka {
        private const val version = "1.4.20"
        const val plugin = "org.jetbrains.dokka:dokka-gradle-plugin:$version"
    }
}
