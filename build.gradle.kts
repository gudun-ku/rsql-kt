plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
}

group = "your.group.id" // Change as appropriate
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", "2.0.21"))
    implementation(kotlin("reflect", "2.0.21"))
    // Add other dependencies here
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
    }
}

tasks.test {
    useJUnitPlatform()
}
