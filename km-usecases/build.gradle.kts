plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("com.vanniktech.maven.publish")
}

group = "app.futured.arkitekt"
/*
 This version supports New Native Memory Model
 https://github.com/JetBrains/kotlin/blob/master/kotlin-native/NEW_MM.md
 */
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
    android {
        publishAllLibraryVariants()
    }

    ios {
        binaries {
            framework {
                baseName = "km-usecases"
            }
        }
    }

    iosSimulatorArm64 {
        binaries {
            framework {
                baseName = "km-usecases"
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
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
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
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by getting {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = app.futured.arkitekt.Project.Android.targetSdkVersion
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = app.futured.arkitekt.Project.Android.minSdkVersion
        targetSdk = app.futured.arkitekt.Project.Android.targetSdkVersion
    }
}
