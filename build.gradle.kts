group = "app.futured.arkitekt"
version = "1.0-SNAPSHOT"
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath(kotlin("gradle-plugin", "1.5.0"))
    }
}
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
