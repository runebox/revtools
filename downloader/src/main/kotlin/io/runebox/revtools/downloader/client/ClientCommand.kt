package io.runebox.revtools.downloader.client

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.file
import org.tinylog.kotlin.Logger
import java.io.File

class ClientCommand : CliktCommand(name = "client") {

    private val revision by option("-r", "--revision").default("latest")

    private val output by option("-o", "--output")
        .file().default(File("client.jar"))

    private val downloadUrl by option("-u", "--url").default(DOWNLOAD_URL)

    private val javConfigUrl by option("-j", "--jav-config").default(JAGEX_URL + "/jav_config.ws")

    override fun run() {
        Logger.info("Downloading Old School RuneScape client jar...")

        val javConfig = JavConfig(javConfigUrl)
        val revision = if(revision == "latest") { javConfig["param_25"] } else revision
        Logger.info("Successfully loaded Jagex remote configurations. Targeting revision: $revision.")

        Logger.info("Saving downloaded client jar to output file: ${output.path}...")

        val downloadUrl = if(revision == "latest") { "https://oldschool.runescape.com/gamepack.jar" } else "$downloadUrl/osrs-$revision.jar"
        val gamepack = Gamepack.Custom(downloadUrl)
        gamepack.save(output)

        Logger.info("Completed download of client jar. Saved to: ${output.path}.")
    }

    companion object {
        const val DOWNLOAD_URL = "https://github.com/runetech/osrs-gamepacks/raw/master/gamepacks"
        const val JAGEX_URL = "https://oldschool.runescape.com"
    }
}