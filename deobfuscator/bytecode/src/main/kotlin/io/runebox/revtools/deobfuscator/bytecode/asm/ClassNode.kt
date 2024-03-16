package io.runebox.revtools.deobfuscator.bytecode.asm

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

fun ClassNode.load() {
    methods.forEach(MethodNode::load)
    fields.forEach(FieldNode::load)
}