// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
<<<<<<< HEAD
    kotlin("jvm")
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(8)
=======
    alias(libs.plugins.google.gms.google.services) apply false
>>>>>>> authentication
}