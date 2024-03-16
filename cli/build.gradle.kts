plugins {
    application
    alias(libs.plugins.shadow)
}

application {
    applicationName = "revtools"
    mainClass.set("io.runebox.revtools.cli.CommandKt")
}

dependencies {
    implementation(projects.downloader)
    implementation(projects.deobfuscator)
    implementation(projects.deobfuscator.bytecode)
    implementation(libs.clikt)
}