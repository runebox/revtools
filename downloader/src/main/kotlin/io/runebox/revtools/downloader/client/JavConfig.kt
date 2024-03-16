package io.runebox.revtools.downloader.client

import org.jsoup.Jsoup
import java.util.*

class JavConfig(val url: String = "https://oldschool.runescape.com/jav_config.ws") {
    val properties: Properties by lazy {
        Properties().also { it.load(
            Jsoup.connect(url)
                .get()
                .wholeText()
                .replace("msg=", "msg_")
                .replace("param=", "param_")
                .byteInputStream()
        ) }
    }

    operator fun get(key: String) = properties.getProperty(key) ?: error("No property $key in jav_config.")
}
