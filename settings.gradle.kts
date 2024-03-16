import org.gradle.api.internal.FeaturePreviews
import org.gradle.api.internal.FeaturePreviews.Feature.TYPESAFE_PROJECT_ACCESSORS

rootProject.name = "revtools"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        mavenLocal()
    }
}

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.23"
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.60.5"
}

enableFeaturePreview(TYPESAFE_PROJECT_ACCESSORS.name)

refreshVersions {
    versionsPropertiesFile = file("gradle/versions.properties")
}

val modules = listOf(
    "cli",
    "asm",
    "downloader",
    "deobfuscator",
    "deobfuscator:bytecode",
    "deobfuscator:ast",
    "deobfuscator:decompiler",
    "deobfuscator:processor",
    "deobfuscator:annotations",
    "logger",
    "client"
)

modules.forEach {
    include(it)
    if(it.contains(":")) {

    }
}

gradle.afterProject {

}