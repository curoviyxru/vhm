group = "moe.crx"
version = "1.0"

plugins {
    java
}

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

        implementation("com.intellij:annotations:12.0")

        implementation("org.projectlombok:lombok:1.18.24")
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")

        implementation("com.google.code.gson:gson:2.9.1")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}