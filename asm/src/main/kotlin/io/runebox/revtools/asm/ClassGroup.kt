package io.runebox.revtools.asm

import io.runebox.revtools.asm.node.*
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.File
import java.util.SortedMap
import java.util.TreeMap
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class ClassGroup {
    private val classMap: SortedMap<String, ClassNode> = TreeMap()
    private val runtimeClassMap: SortedMap<String, ClassNode> = TreeMap()
    private val resources: SortedMap<String, ByteArray> = TreeMap()
    private var hasBuilt = false

    val classes: Set<ClassNode> get() = classMap.values.filter { !it.isIgnored }.filterIsInstanceTo(mutableSetOf())
    val ignoredClasses: Set<ClassNode> get() = classMap.values.filter { it.isIgnored }.filterIsInstanceTo(mutableSetOf())
    val runtimeClasses: Set<ClassNode> get() = runtimeClassMap.values.filterIsInstanceTo(mutableSetOf())
    val allClasses get() = classes.plus(ignoredClasses)

    fun addResource(name: String, bytes: ByteArray) {
        resources[name] = bytes
    }

    fun addClass(cls: ClassNode) {
        cls.init(this)
        val map = if(cls.isRuntime) runtimeClassMap else classMap
        map[cls.name] = cls
    }

    fun removeClass(cls: ClassNode) {
        val map = if(cls.isRuntime) runtimeClassMap else classMap
        map.remove(cls.name)
    }

    fun containsClass(name: String) = classMap.containsKey(name)

    fun getClass(name: String): ClassNode? = classMap[name] ?: runtimeClassMap.getOrPut(name) {
        (ClassLoader.getSystemClassLoader()
            .getResourceAsStream("$name.class")
            ?.readBytes()
            ?.toClassNode(ClassReader.SKIP_FRAMES)
            ?: error("Could not find class: $name in class path.")).also { cls ->
                cls.isRuntime = true
                addClass(cls)
        }
    }

    fun build() {
        if(hasBuilt) allClasses.forEach(ClassNode::reset)
        allClasses.forEach(ClassNode::build)
        hasBuilt = true
    }

    fun clear() {
        classMap.clear()
        runtimeClassMap.clear()
    }

    fun readJar(file: File) {
        JarFile(file).use { jar ->
            for((index, entry) in jar.entries().asSequence().withIndex()) {
                val bytes = jar.getInputStream(entry).readBytes()
                if(entry.name.endsWith(".class")) {
                    val cls = bytes.toClassNode(ClassReader.SKIP_FRAMES)
                    cls.jarIndex = index
                    addClass(cls)
                } else {
                    addResource(entry.name, bytes)
                }
            }
        }
    }

    fun writeJar(file: File, writeIgnored: Boolean = false, writeResources: Boolean = false) {
        if(file.exists()) file.delete()
        if(file.parentFile?.exists() != true) file.parentFile?.mkdirs()

        JarOutputStream(file.outputStream()).use { jos ->
            for(cls in classes) {
                jos.putNextEntry(JarEntry("${cls.name}.class"))
                jos.write(cls.toByteArray(ClassWriter.COMPUTE_MAXS))
                jos.closeEntry()
            }
            if(writeIgnored) {
                for(cls in ignoredClasses) {
                    jos.putNextEntry(JarEntry("${cls.name}.class"))
                    jos.write(cls.toByteArray(ClassWriter.COMPUTE_MAXS))
                    jos.closeEntry()
                }
            }
            if(writeResources) {
                for((name, bytes) in resources) {
                    jos.putNextEntry(JarEntry(name))
                    jos.write(bytes)
                    jos.closeEntry()
                }
            }
        }
    }
}