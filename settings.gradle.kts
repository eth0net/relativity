pluginManagement {
    repositories {
        maven("https://maven.quiltmc.org/repository/release") { name = "Quilt" }
        maven("https://maven.quiltmc.org/repository/snapshot") { name = "Quilt Snapshot" }
        maven("https://maven.fabricmc.net") { name = "Fabric" }
        gradlePluginPortal()
    }
    plugins {
        kotlin("jvm").version(System.getProperty("kotlinVersion"))
    }
}
