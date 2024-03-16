package io.runebox.revtools.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.runebox.revtools.deobfuscator.DeobfuscateCommand
import io.runebox.revtools.downloader.DownloadCommand
import kotlin.streams.asSequence


class Command : CliktCommand(name = "revtools", invokeWithoutSubcommand = true, printHelpOnEmptyArgs = true) {
    init {
        subcommands(
            DownloadCommand(),
            DeobfuscateCommand()
        )
    }

    override fun run() {
        printLogo()
    }
}

private fun printLogo() = """
                                                                      
     ______   __  __   __   __   ______   ______   ______   __  __   
    /\  __ \ /\ \/\ \ /\ \_.\ \ /\  ___\ /\  __ \ /\  __ \ /\_\_\_\  
    \ \  __< \ \ \_\ \\ \ \=.  \\ \  __\ \ \  __< \ \ \/\ \\/_/\_\/_ 
     \ \_\ \_\\ \_____\\ \_\\ \_\\ \_____\\ \_____\\ \_____\ /\_\/\_\
      \/_/ /_/ \/_____/ \/_/ \/_/ \/_____/ \/_____/ \/_____/ \/_/\/_/
      ${"\u001B[38;5;33m"}::: Revision Toolkit :::                               ${"\u001B[38;5;33m"}(0.1.0)${"\u001B[0m"}
${"\u001B[4m"}                                                                                    ${"\u001B[24m"}

""".trimIndent()
    .let { it.replace("/\\", "/\u200E\\") }.apply { println(this) }

fun main(args: Array<String>) = Command().main(args)