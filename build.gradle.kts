import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    jar {
        manifest {
            attributes["Main-Class"] = "Day14.Day14Kt"
        }

        configurations["compileClasspath"].forEach { file: File ->
            from(zipTree(file.absoluteFile))
        }

        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    languageVersion = "1.8"
}