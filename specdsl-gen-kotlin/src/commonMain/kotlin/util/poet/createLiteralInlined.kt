package org.cufy.specdsl.gen.kotlin.util.poet

import com.squareup.kotlinpoet.CodeBlock
import org.cufy.specdsl.*
import org.cufy.specdsl.gen.kotlin.GenGroup

private const val TAG = "createLiteralInlined"

@Marker0
fun GenGroup.createLiteralInlined(element: ConstDefinition): CodeBlock {
    return createLiteralInlined(element.constType, element.constValue)
}

@Marker0
fun GenGroup.createLiteralInlined(element: TypeDefinition, value: Literal): CodeBlock {
    return when (element) {
        is ConstDefinition -> createLiteralInlined(element.constType, value)
        is OptionalDefinition -> createLiteralInlined(element.optionalType, value)
        is ArrayDefinition -> createLiteralInlined(element, value)
        is ScalarDefinition -> createLiteralInlined(element, value)

        is StructDefinition,
        is TupleDefinition,
        is InterDefinition,
        is UnionDefinition,
        -> failGen(TAG, element) { "element not supported" }
    }
}

private fun GenGroup.createLiteralInlined(element: ArrayDefinition, value: Literal): CodeBlock {
    if (value !is TupleLiteral)
        failGen(TAG, element) { "illegal value: $value" }

    val format = buildString {
        append("arrayOf(")
        repeat(value.value.size) { i ->
            if (i != 0) append(", ")
            append("%L")
        }
        append(")")
    }

    val args = Array(value.value.size) { i ->
        createLiteralInlined(element.arrayType, value.value[i])
    }

    return CodeBlock.of(format, *args)
}

private fun GenGroup.createLiteralInlined(element: ScalarDefinition, value: Literal): CodeBlock {
    return when (value) {
        is NullLiteral -> CodeBlock.of("%L", "null")
        is BooleanLiteral -> CodeBlock.of("%L", value.value)
        is IntLiteral -> CodeBlock.of("%L", value.value)
        is FloatLiteral -> CodeBlock.of("%L", value.value)
        is StringLiteral -> CodeBlock.of("%S", value.value)
        is TupleLiteral -> failGen(TAG, element) { "illegal value: $value" }
    }
}
