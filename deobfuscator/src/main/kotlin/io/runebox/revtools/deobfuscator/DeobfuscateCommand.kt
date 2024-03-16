package io.runebox.revtools.deobfuscator

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.runebox.revtools.deobfuscator.bytecode.DeobfuscateBytecodeCommand

fun main(args: Array<String>) = DeobfuscateCommand().main(args)

class DeobfuscateCommand : NoOpCliktCommand(name = "deobfuscate") {
    init {
        subcommands(
            DeobfuscateBytecodeCommand()
        )
    }
}