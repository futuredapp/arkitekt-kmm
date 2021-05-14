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
        classpath(app.futured.arkitekt.Dependencies.MavenPublish.plugin)
        classpath(app.futured.arkitekt.Dependencies.Dokka.plugin)
    }
}

plugins {
    idea
    signing
    publishing
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

project.subprojects {
    plugins.whenPluginAdded {
        if (this is SigningPlugin) {
            extensions.findByType<SigningExtension>()?.apply {
                val hasKey = project.hasProperty("SIGNING_PRIVATE_KEY")
                val hasPassword = project.hasProperty("SIGNING_PASSWORD")

                if (hasKey && hasPassword) {
                    val key = project.properties["SIGNING_PRIVATE_KEY"].toString()
                    val password = project.properties["SIGNING_PASSWORD"].toString()
                    useInMemoryPgpKeys(key, password)
                }
            }
        }
    }
}
