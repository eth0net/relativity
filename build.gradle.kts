import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaVersion = JavaVersion.VERSION_17
val mavenGroup: String by project
val modId: String by project
val modVersion: String by project
val modVersionName: String by project
val modVersionType: String by project
val quiltStandardLibraryId: String by project
val fabricLanguageKotlinId: String by project

val changelogFile = project.file("changelogs/$modVersion.md")
val changelogText = if (changelogFile.exists()) changelogFile.readText() else "No changelog provided."

plugins {
    kotlin("jvm").version(System.getProperty("kotlinVersion"))
    id("com.modrinth.minotaur").version("2.+")
    alias(libs.plugins.quilt.loom)
}

base { archivesName.set(modId) }

group = mavenGroup
version = modVersion

repositories {}

// All the dependencies are declared at gradle/libs.version.toml and referenced with "libs.<id>"
// See https://docs.gradle.org/current/userguide/platforms.html for information on how version catalogs work.
dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:${libs.versions.quilt.mappings.get()}:v2"))
    })
    modImplementation(libs.quilt.loader)
    modImplementation(libs.quilted.fabric.api)
    modImplementation(libs.fabric.language.kotlin)
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.release.set(javaVersion.toString().toInt())
    }

    withType<KotlinCompile> { kotlinOptions { jvmTarget = javaVersion.toString() } }

    processResources {
        inputs.property("version", version)
        filesMatching("quilt.mod.json") { expand(mapOf("version" to version)) }
    }

    jar { from("LICENSE") { rename { "${it}_${base.archivesName}" } } }

    java {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        withSourcesJar()
    }

    modrinth {
        token.set(System.getenv("MODRINTH_TOKEN"))
        loaders.set(listOf("quilt"))
        gameVersions.set(listOf("1.19", "1.19.1", "1.19.2"))
        projectId.set(base.archivesName)
        versionName.set(modVersionName)
        versionType.set(modVersionType)
        changelog.set(changelogText)
        uploadFile.set(remapJar.get())
        additionalFiles.set(listOf(remapSourcesJar.get()))
        dependencies {
            required.version(quiltStandardLibraryId)
            required.version(fabricLanguageKotlinId)
        }
    }
}
