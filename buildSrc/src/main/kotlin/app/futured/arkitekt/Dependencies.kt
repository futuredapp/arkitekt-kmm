package app.futured.arkitekt

object Dependencies {
    object Kotlin {
        const val version = "1.5.0"

        const val gradlePlugin = "gradle-plugin"
    }
    object Android {
        private const val agpVersion = "4.1.1"

        const val gradlePlugin = "com.android.tools.build:gradle:$agpVersion"
    }
}