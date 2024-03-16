import de.fayard.refreshVersions.RefreshVersionsMigrateTask
import de.fayard.refreshVersions.core.versionFor
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

defaultTasks("build")

plugins {
    alias(libs.plugins.kotlin.jvm)
    base
}

tasks.wrapper {
    gradleVersion = versionFor("version.gradle")
}

allprojects {
    group = "io.runebox.revtoosl"
    version = "0.1.0-r220-1"

    plugins.withType<BasePlugin> {
        configure<BasePluginExtension> {
            archivesName.set("${rootProject.name}-$name")
        }
    }

    plugins.withType<ApplicationPlugin> {
        tasks.named<JavaExec>("run") {
            standardInput = System.`in`
            workingDir = rootProject.projectDir
        }
    }

    plugins.withType<JavaPlugin> {
        configure<JavaPluginExtension> {
            withSourcesJar()
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

configure(subprojects.filter { it.name != "client" }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
    }
}