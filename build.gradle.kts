group = "moe.crx"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
}

allprojects {
    apply {
        plugin("java")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

        implementation("org.jetbrains:annotations:23.0.0")
        implementation("com.google.inject:guice:5.1.0")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}