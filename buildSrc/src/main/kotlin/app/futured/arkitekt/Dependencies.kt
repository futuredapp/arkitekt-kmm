package app.futured.arkitekt

object Dependencies {
    object Kotlin {
        const val version = "1.5.0"

        const val gradlePlugin = "gradle-plugin"
    }

    object KotlinX {
        const val coroutinesMt = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt"
        const val serialization = "org.jetbrains.kotlin:kotlin-serialization:1.5.10"
    }

    object Android {
        private const val agpVersion = "4.1.1"

        const val gradlePlugin = "com.android.tools.build:gradle:$agpVersion"
    }

    object MavenPublish {
        private const val version = "0.14.2"
        const val plugin = "com.vanniktech:gradle-maven-publish-plugin:$version"
    }

    object Dokka {
        private const val version = "1.4.20"
        const val plugin = "org.jetbrains.dokka:dokka-gradle-plugin:$version"
    }
}