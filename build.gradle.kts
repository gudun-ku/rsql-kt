plugins {
    kotlin("jvm") version "2.0.21"
    antlr
    `java-library`
}

group = "com.beloushkin"
version = "2.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", "2.0.21"))
    implementation(kotlin("reflect", "2.0.21"))
    // ANTLR4
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    // Add other dependencies here
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
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
