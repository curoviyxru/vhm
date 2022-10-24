group = "moe.crx"
version = "1.0-SNAPSHOT"

plugins {
    application
}

application {
    mainClass.set("moe.crx.Starter")
}

dependencies {
    implementation(project(":modules"))
}

tasks.getByName<JavaExec>("run") {
    standardInput = System.`in`
}