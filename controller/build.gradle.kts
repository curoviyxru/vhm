plugins {
    id("java")
    application
}

group = "moe.crx"
version = "1.0"

application {
    mainClass.set("moe.crx.Application")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation(project(":models"))
    implementation("com.intellij:annotations:12.0")
    implementation("com.google.code.gson:gson:2.9.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}