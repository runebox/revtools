package io.runebox.revtools.asm

import io.runebox.revtools.asm.node.owner
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

data class MemberRef(val owner: String, val name: String, val desc: String) : Comparable<MemberRef> {
    constructor(method: MethodNode) : this(method.owner.name, method.name, method.desc)
    constructor(field: FieldNode) : this(field.owner.name, field.name, field.desc)
    constructor(owner: ClassNode, method: MethodNode) : this(owner.name, method.name, method.desc)
    constructor(owner: ClassNode, field: FieldNode) : this(owner.name, field.name, field.desc)
    constructor(insn: MethodInsnNode) : this(insn.owner, insn.name, insn.desc)
    constructor(insn: FieldInsnNode) : this(insn.owner, insn.name, insn.desc)
    constructor(owner: String, member: MemberDesc) : this(owner, member.name, member.desc)

    override fun compareTo(other: MemberRef): Int {
        var result = owner.compareTo(other.owner)
        if(result != 0) return result
        result = name.compareTo(other.name)
        if(result != 0) return result
        return desc.compareTo(other.desc)
    }

    override fun toString(): String {
        return "$owner.$name $desc"
    }
}