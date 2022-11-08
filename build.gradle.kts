plugins {
    id("java")
    application
}

group = "moe.crx"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("moe.crx.Starter")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.flywaydb:flyway-core:9.6.0")
    implementation("org.postgresql:postgresql:42.5.0")

    implementation("org.projectlombok:lombok:1.18.24")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}