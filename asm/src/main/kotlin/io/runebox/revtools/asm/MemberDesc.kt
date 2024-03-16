package io.runebox.revtools.asm

import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

data class MemberDesc(val name: String, val desc: String) {
    constructor(method: MethodNode) : this(method.name, method.desc)
    constructor(field: FieldNode) : this(field.name, field.desc)
    constructor(insn: MethodInsnNode) : this(insn.name, insn.desc)
    constructor(insn: FieldInsnNode) : this(insn.name, insn.desc)
    constructor(member: MemberRef) : this(member.name, member.desc)

    override fun toString(): String {
        return "$name $desc"
    }
}