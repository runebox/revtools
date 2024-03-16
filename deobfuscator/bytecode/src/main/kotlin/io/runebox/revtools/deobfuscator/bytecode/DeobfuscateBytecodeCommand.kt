package io.runebox.revtools.deobfuscator.bytecode

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file

class DeobfuscateBytecodeCommand : CliktCommand(name = "bytecode") {

    private val inputJar by argument("input-jar").file(canBeDir = false, mustExist = true)
    private val outputJar by argument("output-jar").file(canBeDir = false)

    private val testClient by option("-t", "--test-client").flag(default = false)

    override fun run() {
        Deobfuscator(
            inputJar,
            outputJar
        ).run()
    }
}