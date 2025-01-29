plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.4"
}

group = "com.utility"
version = "1.0"

repositories {
    mavenCentral()
}

// Configure Gradle IntelliJ Plugin
intellij {
    version.set("2023.2.6")
    type.set("IC") // Target IDE Platform (IC for IntelliJ Community)
    plugins.set(listOf("java"))
}

dependencies {
    implementation(kotlin("stdlib")) // Add Kotlin standard library
}

// Source sets for Java code
sourceSets {
    main {
        java {
            setSrcDirs(listOf("src/main/java")) // Ensure Java sources are correctly set up
        }
    }
}

// JVM compatibility with JDK 17 (using Kotlin's supported JVM target)
tasks {
    withType<JavaCompile> {
        sourceCompatibility = "17"  // Use JDK 17
        targetCompatibility = "17"  // Use JDK 17
    }

    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"  // Ensure Kotlin uses JDK 17
    }

    // Modify the plugin.xml for compatibility with specific IntelliJ IDEA builds
    patchPluginXml {
        sinceBuild.set("232")
        untilBuild.set("242.*")
    }

    // Signing plugin (used for publishing)
    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    // Publishing plugin
    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}
