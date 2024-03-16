package io.runebox.revtools.asm.node

import io.runebox.revtools.asm.util.field
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.MethodNode
import org.objectweb.asm.tree.TryCatchBlockNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter

var MethodNode.owner: ClassNode by field()
val MethodNode.group get() = owner.group

fun MethodNode.init(owner: ClassNode) {
    this.owner = owner
}

fun MethodNode.removeDeadCode() {
    var changed: Boolean
    do {
        changed = false

        val frames = Analyzer(BasicInterpreter()).analyze(owner.name, this)
        val insns = instructions.iterator()
        var index = 0

        for(insn in insns) {
            if(frames[index++] != null && insn is LabelNode) {
                continue
            }

            insns.remove()
            changed = true
        }

        changed = changed or tryCatchBlocks.removeIf { it.isBodyEmpty() }
    } while(changed)
}

fun TryCatchBlockNode.isBodyEmpty(): Boolean {
    var cur = start.next
    while(true) {
        when {
            cur == null -> error("Failed to reach the end label of Try-Cach block.")
            cur === end -> return true
            cur.opcode != -1 -> return false
            else -> cur = cur.next
        }
    }
}