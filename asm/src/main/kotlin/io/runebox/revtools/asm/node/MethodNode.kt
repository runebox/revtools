package io.runebox.revtools.asm.node

import io.runebox.revtools.asm.util.field
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

var MethodNode.owner: ClassNode by field()
val MethodNode.group get() = owner.group

fun MethodNode.init(owner: ClassNode) {
    this.owner = owner
}