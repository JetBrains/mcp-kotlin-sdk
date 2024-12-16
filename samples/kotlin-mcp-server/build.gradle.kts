plugins {
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    application
}

application {
    mainClass.set("MainKt")
}


group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.github.JetBrains:mcp-kotlin-sdk:b1b0238")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}