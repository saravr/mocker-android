plugins {
    kotlin("jvm") version "1.8.10"
    id("maven-publish")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("jar") {
        groupId = "com.sandymist.android.mocker"
        artifactId = "config"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}

