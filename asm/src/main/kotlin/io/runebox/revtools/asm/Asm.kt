package io.runebox.revtools.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.ClassNode

fun Int.isAbstract() = (this and ACC_ABSTRACT) != 0
fun Int.isPrivate() = (this and ACC_PRIVATE) != 0
fun Int.isPublic() = (this and ACC_PUBLIC) != 0
fun Int.isInterface() = (this and ACC_INTERFACE) != 0
fun Int.isStatic() = (this and ACC_STATIC) != 0

fun ByteArray.toClassNode(flags: Int): ClassNode {
    val reader = ClassReader(this)
    val cls = ClassNode()
    reader.accept(cls, flags)
    return cls
}

fun ClassNode.toByteArray(flags: Int): ByteArray {
    val writer = ClassWriter(flags)
    this.accept(writer)
    return writer.toByteArray()
}