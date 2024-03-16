package io.runebox.revtools.asm.node

import io.runebox.revtools.asm.ClassGroup
import io.runebox.revtools.asm.util.field
import io.runebox.revtools.asm.util.mutableSetField
import io.runebox.revtools.asm.util.nullField
import org.objectweb.asm.tree.ClassNode

var ClassNode.group: ClassGroup by field()
var ClassNode.isRuntime: Boolean by field { false }
var ClassNode.isIgnored: Boolean by field { false }
var ClassNode.jarIndex: Int by field { -1 }

var ClassNode.superClass: ClassNode? by nullField()
val ClassNode.interfaceClasses: MutableSet<ClassNode> by mutableSetField()
val ClassNode.childClasses: MutableSet<ClassNode> by mutableSetField()
val ClassNode.parentClasses get() = listOfNotNull(superClass).plus(interfaceClasses)

val ClassNode.allParentClasses: Set<ClassNode> get() = parentClasses.flatMap { it.allParentClasses.plus(it) }.toSet()
val ClassNode.allChildClasses: Set<ClassNode> get() = childClasses.flatMap { it.allChildClasses.plus(it) }.toSet()

fun ClassNode.init(group: ClassGroup) {
    this.group = group
    methods.forEach { it.init(this) }
    fields.forEach { it.init(this) }
}

fun ClassNode.reset() {
    superClass = null
    interfaceClasses.clear()
    childClasses.clear()
}

fun ClassNode.build() {
    superClass = superName?.let { group.getClass(it) }
    superClass?.childClasses?.add(this)
    interfaceClasses.addAll(interfaces.mapNotNull { group.getClass(it) })
    interfaceClasses.forEach { it.childClasses.add(this) }
}