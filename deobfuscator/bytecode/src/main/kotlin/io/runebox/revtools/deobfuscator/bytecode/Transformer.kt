package io.runebox.revtools.deobfuscator.bytecode

import io.runebox.revtools.asm.ClassGroup

interface Transformer {

    val name get() = this::class.simpleName!!.replace("Transformer", "")

    fun transform(group: ClassGroup)

    fun postTransform(group: ClassGroup) { /* Nothing */ }

    companion object
}