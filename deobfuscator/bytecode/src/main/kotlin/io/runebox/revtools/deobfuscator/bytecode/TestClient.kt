@file:Suppress("DEPRECATION", "removal")

package io.runebox.revtools.deobfuscator.bytecode

import java.applet.Applet
import java.applet.AppletContext
import java.applet.AppletStub
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import javax.swing.JFrame
import kotlin.reflect.full.createInstance

class TestClient(private val file: File) {

    private val params = hashMapOf<String, String>()

    fun start() {
        // Fetch JAV_CONFIG params
        val lines = URL("http://oldschool1.runescape.com/jav_config.ws")
            .openConnection()
            .getInputStream()
            .bufferedReader()
            .readLines()
        lines.forEach {
            var line = it
            if(line.startsWith("param=")) {
                line = line.substring(6)
            }
            val idx = line.indexOf("=")
            if(idx >= 0) {
                params[line.substring(0, idx)] = line.substring(idx + 1)
            }
        }

        // Load the client class as an Applet from the deob output jar.
        val classLoader = URLClassLoader(arrayOf(file.toURI().toURL()))
        val main = params["initial_class"]!!.replace(".class", "")
        val applet = classLoader.loadClass(main).kotlin.createInstance() as Applet
        applet.background = Color.BLACK
        applet.layout = null
        applet.size = Dimension(params["applet_minwidth"]!!.toInt(), params["applet_minheight"]!!.toInt())
        applet.preferredSize = applet.size
        applet.createStub()
        applet.init()

        // Create a frame/winidow with the applet.
        val frame = JFrame("Test Client - ${file.name} - Revision ${params["25"]}")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.layout = GridLayout(1, 0)
        frame.add(applet)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
        frame.minimumSize = frame.size
    }

    private fun Applet.createStub() {
        val stub = object : AppletStub {
            override fun isActive(): Boolean = true
            override fun getAppletContext(): AppletContext? = null
            override fun getParameter(name: String) = params[name]
            override fun appletResize(width: Int, height: Int) { this@createStub.size = Dimension(width, height) }
            override fun getCodeBase(): URL = URL(params["codebase"])
            override fun getDocumentBase(): URL = codeBase
        }
        setStub(stub)
    }
}