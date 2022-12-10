plugins {
    id("java")
    application
}

group = "moe.crx"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":verticles"))
}

application {
    mainClass.set("moe.crx.Starter")
}

allprojects {
    apply {
        plugin("java")
    }

    group = "moe.crx"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("io.vertx:vertx-core:4.3.5")
        implementation("io.vertx:vertx-hazelcast:4.3.5")

        implementation("org.jetbrains:annotations:23.0.0")

        implementation("org.projectlombok:lombok:1.18.24")
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")

        implementation("com.fasterxml.jackson.core:jackson-databind:2.14.0")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}