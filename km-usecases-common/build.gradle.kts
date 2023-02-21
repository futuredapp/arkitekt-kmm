plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

group = "app.futured.arkitekt"
version = "1.0-SNAPSHOT"

repositories {
    // for dokkaHtml, todo remove when dokkaHtml is available at mavenCentral (ver 0.7.3 isn't)
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
    mavenCentral()
}

val dokkaOutputDir = "$buildDir/dokka"

tasks.getByName<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
    outputDirectory.set(file(dokkaOutputDir))
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}

kotlin {

    android {
        publishAllLibraryVariants()
    }
    ios {
        binaries {
            framework {
                baseName = "km-usecases-common"
            }
        }
    }

    iosSimulatorArm64 {
        binaries {
            framework {
                baseName = "km-usecases-common"
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(app.futured.arkitekt.Dependencies.KotlinX.coroutines)
            }
        }
        val androidMain by getting
    }
}

android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
}
