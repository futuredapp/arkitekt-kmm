plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

group = "app.futured.arkitekt"
version = "0.3.2-SNAPSHOT"

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
    ios {
        binaries {
            framework {
                baseName = "km-viewmodel"
            }
        }
    }
    iosSimulatorArm64 {
        binaries {
            framework {
                baseName = "km-viewmodel"
            }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(app.futured.arkitekt.Dependencies.KotlinX.coroutines)
                implementation(project(":km-usecases"))
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}
