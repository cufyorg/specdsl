package org.cufy.specdsl.gen.kotlin.core

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import org.cufy.specdsl.ConstDefinition
import org.cufy.specdsl.ScalarDefinition
import org.cufy.specdsl.TupleLiteral
import org.cufy.specdsl.gen.kotlin.GenContext
import org.cufy.specdsl.gen.kotlin.GenGroup
import org.cufy.specdsl.gen.kotlin.util.asReferenceName
import org.cufy.specdsl.gen.kotlin.util.createKDoc
import org.cufy.specdsl.gen.kotlin.util.poet.createAnnotationSet
import org.cufy.specdsl.gen.kotlin.util.poet.createLiteral
import org.cufy.specdsl.gen.kotlin.util.poet.typeOf

class ConstDefinitionGen(override val ctx: GenContext) : GenGroup() {
    fun generateConstants() {
        for (element in ctx.specSheet.collectChildren()) {
            if (element !is ConstDefinition) continue
            if (element.isAnonymous) continue
            if (element.canonicalName in ctx.nativeElements) continue

            failGenBoundary {
                generateLiteralConstant(element)
            }
        }
    }

    /**
     * Generate fields for the values of constant definitions.
     *
     * ### Skipped for:
     *
     * - anonymous elements
     * - native elements
     *
     * ### Example:
     *
     * ```
     * val custom.example.something by const(builtin.String, "Hello World".literal)
     * ```
     *
     * ### Produces:
     *
     * ```
     * // classes["builtin.String"] = "kotlin.String"
     * // nativeElements += "builtin.String"
     *
     * inline val custom_example.something: kotlin.String get() = "Hello World"
     * ```
     */
    private fun generateLiteralConstant(element: ConstDefinition) {
        val isCompileTimeConstant = when {
            element.constType !is ScalarDefinition -> false
            element.constType.canonicalName !in ctx.nativeElements -> false
            element.constValue is TupleLiteral -> false
            else -> true
        }

        onObject(element.namespace) {
            val propertySpec = PropertySpec
                .builder(element.asReferenceName, typeOf(element))
                .addKdoc(createKDoc(element))
                .addAnnotations(createAnnotationSet(element.metadata))
                .apply {
                    if (isCompileTimeConstant)
                        addModifiers(KModifier.CONST)
                }
                .initializer(createLiteral(element))
                .build()

            addProperty(propertySpec)
        }
    }
}
