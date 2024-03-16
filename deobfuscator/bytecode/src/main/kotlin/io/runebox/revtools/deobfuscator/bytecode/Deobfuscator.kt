package io.runebox.revtools.deobfuscator.bytecode

import io.runebox.revtools.asm.ClassGroup
import io.runebox.revtools.asm.node.isIgnored
import io.runebox.revtools.deobfuscator.bytecode.transformer.DeadCodeTransformer
import io.runebox.revtools.deobfuscator.bytecode.transformer.RuntimeExceptionTransformer
import me.tongfei.progressbar.DelegatingProgressBarConsumer
import me.tongfei.progressbar.ProgressBarBuilder
import me.tongfei.progressbar.ProgressBarStyle
import org.tinylog.kotlin.Logger
import java.io.File
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createInstance

class Deobfuscator(
    private val inputJar: File,
    private val outputJar: File
) {
    private val group = ClassGroup()
    private val transformers = mutableListOf<Transformer>()

    private fun init() {
        Logger.info("Initializing bytecode transformer...")

        /**
         * Load all the bytecode transformers.
         */
        register<RuntimeExceptionTransformer>()
        register<DeadCodeTransformer>()

        Logger.info("Registered ${transformers.size} bytecode transformers.")

        /**
         * Load all the classes from the input jar into group
         */
        Logger.info("Loading classes from input jar: ${inputJar.path}.")

        group.readJar(inputJar)
        group.build()

        // Ignore all the bouncy castle library classes but dont exclude them from the class group.
        for(cls in group.classes.filter { it.name.startsWith("org/") }) {
            cls.isIgnored = true
        }

        Logger.info("Loaded ${group.classes.size} classes. (${group.ignoredClasses.size} ignored)")
    }

    fun run() {
        this.init()

        Logger.info("Starting bytecode deobfuscator transformers...")

        ProgressBarBuilder()
            .setMaxRenderedLength(200)
            .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
            .setInitialMax(transformers.size.toLong())
            .setConsumer(DelegatingProgressBarConsumer(Logger::info))
            .build().use { progressBar ->
                val start = System.currentTimeMillis()

                for(transformer in transformers) {
                    Logger.info("Running transformer: '${transformer.name}'.")

                    transformer.transform(group)
                    transformer.postTransform(group)

                    progressBar.step()
                }

                val delta = System.currentTimeMillis() - start
                Logger.info("Completed all bytecode transformers in ${delta/1000L} seconds.")

                // Close progress if not already done.
            }

        Logger.info("Writing deobfuscated classes to output jar: ${outputJar.path}.")

        group.writeJar(outputJar, writeIgnored = true, writeResources = false)

        Logger.info("Bytecode deobfuscator has successfully completed.")
    }

    @DslMarker
    private annotation class TransformerDslMarker

    @TransformerDslMarker
    private inline fun <reified T : Transformer> register() {
        transformers.add(T::class.createInstance())
    }
}