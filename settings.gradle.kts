pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:4.1.1")
            }
        }
    }
}
rootProject.name = "arkitekt-kmm"
include("km-usecases")
include("km-viewmodel")
