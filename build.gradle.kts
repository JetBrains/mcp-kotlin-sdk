import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
    `maven-publish`
}

group = "org.jetbrains.kotlinx.mcp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.apache)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.sse)
    implementation(libs.ktor.server.websockets)
    implementation(libs.kotlin.logging)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.coroutines.debug)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
        }
    }
}

tasks.create<Jar>("sourcesJar") {
    from(sourceSets["main"].allSource)
    archiveClassifier.set("sources")
}

tasks.test {
    useJUnitPlatform()
}

abstract class GenerateLibVersionTask @Inject constructor(
    @get:Input val libVersion: String,
    @get:OutputDirectory val sourcesDir: File
) : DefaultTask() {
    @TaskAction
    fun generate() {
        val sourceFile = File(sourcesDir, "LibVersion.kt")

        sourceFile.writeText(
            """
            package shared

            const val LIB_VERSION = "$libVersion"

            """.trimIndent()
        )
    }
}

dokka {
    moduleName.set("MCP Kotlin SDK")

    dokkaSourceSets.main {
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl("https://github.com/e5l/mcp-kotlin-sdk")
            remoteLineSuffix.set("#L")
            documentedVisibilities(VisibilityModifier.Public)
        }
    }
    dokkaPublications.html {
        outputDirectory.set(project.layout.projectDirectory.dir("docs"))
    }
}

val sourcesDir = File(project.layout.buildDirectory.asFile.get(), "generated-sources/libVersion")

val generateLibVersionTask =
    tasks.register<GenerateLibVersionTask>("generateLibVersion", version.toString(), sourcesDir)

kotlin {
    jvmToolchain(21)

    sourceSets {
        main {
            kotlin.srcDir(generateLibVersionTask.map { it.sourcesDir })
        }
    }
}
