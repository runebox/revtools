package io.runebox.revtools.deobfuscator.bytecode.transformer

import io.runebox.revtools.asm.ClassGroup
import io.runebox.revtools.asm.node.owner
import io.runebox.revtools.deobfuscator.bytecode.Transformer
import org.objectweb.asm.Opcodes.JSR
import org.objectweb.asm.Opcodes.RET
import org.objectweb.asm.util.Printer

class DeadCodeTransformer : Transformer {

    override fun transform(group: ClassGroup) {
        for(method in group.classes.flatMap { it.methods }) {
            for(insn in method.instructions) {
                if(insn.opcode == RET || insn.opcode == JSR) {
                    error("Found ${Printer.OPCODES[insn.opcode]} opcode. (${method.owner.name}.${method.name}${method.desc}")
                }
            }
        }
    }
}