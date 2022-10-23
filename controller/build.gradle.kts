group = "moe.crx"
version = "1.0"

plugins {
    application
}

application {
    mainClass.set("moe.crx.Application")
}

dependencies {
    implementation(project(":models"))
}