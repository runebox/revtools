package io.runebox.revtools.downloader.client

import org.jsoup.Jsoup
import java.io.File

sealed class Gamepack(
    private val bytes: ByteArray,
    private val initialClass: String
) {
    fun save(file: File) {
        if(file.exists()) file.deleteRecursively()
        if(file.parentFile != null) file.parentFile.mkdirs()
        file.createNewFile()
        file.outputStream().use { output ->
            output.write(bytes)
        }
    }

    class Jagex(val javConfig: JavConfig = JavConfig()) : Gamepack(
        Jsoup.connect(javConfig["codebase"] + javConfig["initial_jar"])
            .maxBodySize(0)
            .ignoreContentType(true)
            .execute()
            .bodyAsBytes(),
        javConfig["initial_class"]
    )

    class Custom(val downloadUrl: String) : Gamepack(
        Jsoup.connect(downloadUrl)
            .maxBodySize(0)
            .ignoreContentType(true)
            .execute()
            .bodyAsBytes(),
        "client.class"
    )
}