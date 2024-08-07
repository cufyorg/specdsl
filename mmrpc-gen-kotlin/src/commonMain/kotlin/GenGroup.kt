package org.cufy.mmrpc.gen.kotlin

import com.squareup.kotlinpoet.TypeSpec
import org.cufy.mmrpc.ElementDefinition
import org.cufy.mmrpc.Marker3
import org.cufy.mmrpc.gen.kotlin.util.asClassName

abstract class GenGroup {
    abstract val ctx: GenContext

    abstract fun apply()

    @Marker3
    inline fun failGenBoundary(block: () -> Unit) {
        try {
            block()
        } catch (e: GenException) {
            ctx.failures += e
        }
    }

    @Marker3
    fun failGen(tag: String, definition: ElementDefinition, message: () -> String): Nothing {
        val failure = GenFailure(
            group = this::class.simpleName.orEmpty(),
            tag = tag,
            message = message(),
            element = definition,
        )

        throw GenException(failure)
    }

    @Marker3
    fun create(element: ElementDefinition, block: () -> TypeSpec.Builder) {
        ctx.createElementNodes += CreateElementNode(
            element = element,
            block = block,
        )
    }

    @Marker3
    fun createObject(element: ElementDefinition, block: TypeSpec.Builder.() -> Unit) {
        create(element) {
            TypeSpec.objectBuilder(element.asClassName)
                .apply(block)
        }
    }

    @Marker3
    fun createEnum(element: ElementDefinition, block: TypeSpec.Builder.() -> Unit) {
        create(element) {
            TypeSpec.enumBuilder(element.asClassName)
                .apply(block)
        }
    }

    @Marker3
    fun createClass(element: ElementDefinition, block: TypeSpec.Builder.() -> Unit) {
        create(element) {
            TypeSpec.classBuilder(element.asClassName)
                .apply(block)
        }
    }

    @Marker3
    fun createInterface(element: ElementDefinition, block: TypeSpec.Builder.() -> Unit) {
        create(element) {
            TypeSpec.interfaceBuilder(element.asClassName)
                .apply(block)
        }
    }

    @Marker3
    fun createAnnotation(element: ElementDefinition, block: TypeSpec.Builder.() -> Unit) {
        create(element) {
            TypeSpec.annotationBuilder(element.asClassName)
                .apply(block)
        }
    }

    @Marker3
    fun on(element: ElementDefinition, block: TypeSpec.Builder.() -> Unit) {
        ctx.onElementNodes += OnElementNode(
            element = element,
            block = block,
        )
    }
}
