package io.runebox.revtools.deobfuscator.bytecode.asm

import io.runebox.revtools.asm.ClassGroup
import org.objectweb.asm.tree.ClassNode

val ClassGroup.deobClasses: Set<ClassNode> get() = classes.filter { it.name.startsWith("org/") }.filterIsInstanceTo(mutableSetOf())