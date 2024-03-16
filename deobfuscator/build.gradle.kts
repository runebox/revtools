plugins {
    application
}

application {
    mainClass.set("io.runebox.revtools.deobfuscator.DeobfuscateCommandKt")
}

dependencies {
    api(projects.deobfuscator.bytecode)
    implementation(libs.clikt)
}