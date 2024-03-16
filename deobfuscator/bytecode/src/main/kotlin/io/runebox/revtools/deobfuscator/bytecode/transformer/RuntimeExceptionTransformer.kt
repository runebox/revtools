package io.runebox.revtools.deobfuscator.bytecode.transformer

import io.runebox.revtools.asm.ClassGroup
import io.runebox.revtools.asm.node.removeDeadCode
import io.runebox.revtools.deobfuscator.bytecode.Transformer
import org.objectweb.asm.Type
import org.tinylog.kotlin.Logger
import java.lang.RuntimeException

class RuntimeExceptionTransformer : Transformer {

    private var count = 0

    override fun transform(group: ClassGroup) {
        for(cls in group.classes) {
            for(method in cls.methods) {
                val tcbs = method.tryCatchBlocks.filter { it.type == Type.getInternalName(RuntimeException::class.java) }.iterator()
                for(tcb in tcbs) {
                    method.tryCatchBlocks.remove(tcb)
                    count++
                }
            }
        }
    }

    override fun postTransform(group: ClassGroup) {
        Logger.info("Removed $count 'RuntimeException' try-catch blocks.")
    }

    companion object
}