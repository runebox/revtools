package io.runebox.revtools.asm.node

import io.runebox.revtools.asm.util.field
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode

var FieldNode.owner: ClassNode by field()
val FieldNode.group get() = owner.group

fun FieldNode.init(owner: ClassNode) {
    this.owner = owner
}