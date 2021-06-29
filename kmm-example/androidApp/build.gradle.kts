plugins {
    kotlin("multiplatform")
    id("com.android.application")
}

repositories {
    google()
    jcenter()
}

kotlin {
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":kmm-example:shared"))
                implementation("com.google.android.material:material:1.2.1")
                implementation("androidx.appcompat:appcompat:1.2.0")
                implementation("androidx.constraintlayout:constraintlayout:2.0.2")
            }
        }
    }
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "app.futured.arkitekt.kmmexample.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    lintOptions {
        isAbortOnError = false
    }
}
