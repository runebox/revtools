import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

dependencies {
    api(libs.bundles.asm)
    implementation(libs.guava)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xcontext-receivers"
    }
}