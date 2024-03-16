package io.runebox.revtools.downloader

import com.github.ajalt.clikt.core.NoOpCliktCommand
import com.github.ajalt.clikt.core.subcommands
import io.runebox.revtools.downloader.client.ClientCommand

fun main(args: Array<String>) = DownloadCommand().main(args)

class DownloadCommand : NoOpCliktCommand(name = "download") {
    init {
        subcommands(
            ClientCommand(),
            CacheCommand(),
            XteasCommand()
        )
    }
}