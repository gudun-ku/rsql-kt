val kotlinVersion: String by project
val antlrVersion: String by project

plugins {
    alias(libs.plugins.kotlin.jvm)
    antlr
    `java-library`
}

group = "com.beloushkin"
version = project.property("version") as String

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)
    // ANTLR4
    antlr(libs.antlr4)
    implementation(libs.antlr4.runtime)

    // Add other dependencies here
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:${property("junitJupiterVersion")}")
}

kotlin {
    jvmToolchain(21)
}

tasks {
    generateGrammarSource {
        arguments = arguments + listOf("-visitor", "-no-listener")
        outputDirectory = File("${'$'}{project.buildDir}/generated-src/antlr/main")
    }
    
    compileKotlin {
        dependsOn(generateGrammarSource)
    }
    
    compileTestKotlin {
    }
    
    test {
        useJUnitPlatform()
    }
    
    // Ensure test Kotlin compilation waits for test ANTLR grammar generation
    named("compileTestKotlin") {
        dependsOn("generateTestGrammarSource")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

sourceSets {
    main {
        antlr {
            srcDir("src/main/antlr")
        }
        java {
            srcDir("${'$'}{project.buildDir}/generated-src/antlr/main")
        }
    }
}
