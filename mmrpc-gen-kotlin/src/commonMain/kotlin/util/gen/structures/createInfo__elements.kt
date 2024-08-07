package org.cufy.mmrpc.gen.kotlin.util.gen.structures

import com.squareup.kotlinpoet.CodeBlock
import org.cufy.mmrpc.*
import org.cufy.mmrpc.gen.kotlin.GenGroup
import org.cufy.mmrpc.gen.kotlin.util.gen.references.refOfINFOOrCreateInfo
import org.cufy.mmrpc.gen.kotlin.util.poet.createBoxedLiteral
import org.cufy.mmrpc.gen.kotlin.util.poet.createBoxedNamespace
import org.cufy.mmrpc.gen.kotlin.util.poet.createCall
import org.cufy.mmrpc.gen.kotlin.util.poet.createCallSingleVararg

@Marker3
fun GenGroup.createInfo(element: ConstDefinition): CodeBlock {
    return createCall(
        function = CodeBlock.of("%T", ConstInfo::class),
        "name" to CodeBlock.of("%S", element.name),
        "namespace" to createBoxedNamespace(element.namespace),
        "metadata" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadata.map { createInfoUsage(it) }
        ),
        "type" to refOfINFOOrCreateInfo(element.constType),
        "value" to createBoxedLiteral(element.constValue),
    )
}

@Marker3
fun GenGroup.createInfo(element: FaultDefinition): CodeBlock {
    return createCall(
        function = CodeBlock.of("%T", FaultInfo::class),
        "name" to CodeBlock.of("%S", element.name),
        "namespace" to createBoxedNamespace(element.namespace),
        "metadata" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadata.map { createInfoUsage(it) }
        ),
    )
}

@Marker3
fun GenGroup.createInfo(element: FieldDefinition): CodeBlock {
    return createCall(
        function = CodeBlock.of("%T", FieldInfo::class),
        "name" to CodeBlock.of("%S", element.name),
        "namespace" to createBoxedNamespace(element.namespace),
        "metadata" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadata.map { createInfoUsage(it) }
        ),
        "type" to refOfINFOOrCreateInfo(element.fieldType),
        "default" to element.fieldDefault.let {
            if (it == null) CodeBlock.of("null")
            else createBoxedLiteral(it)
        },
    )
}

@Marker3
fun GenGroup.createInfo(element: MetadataDefinition): CodeBlock {
    return createCall(
        function = CodeBlock.of("%T", MetadataInfo::class),
        "name" to CodeBlock.of("%S", element.name),
        "namespace" to createBoxedNamespace(element.namespace),
        "metadata" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadata.map { createInfoUsage(it) }
        ),
        "fields" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadataFields.map { createInfo(it) }
        ),
    )
}

@Marker3
fun GenGroup.createInfo(element: ProtocolDefinition): CodeBlock {
    return createCall(
        function = CodeBlock.of("%T", ProtocolInfo::class),
        "name" to CodeBlock.of("%S", element.name),
        "namespace" to createBoxedNamespace(element.namespace),
        "metadata" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadata.map { createInfoUsage(it) }
        ),
        "routines" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.protocolRoutines.map { refOfINFOOrCreateInfo(it) }
        )
    )
}

@Marker3
fun GenGroup.createInfo(element: RoutineDefinition): CodeBlock {
    return createCall(
        function = CodeBlock.of("%T", RoutineInfo::class),
        "name" to CodeBlock.of("%S", element.name),
        "namespace" to createBoxedNamespace(element.namespace),
        "metadata" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.metadata.map { createInfoUsage(it) }
        ),
        "endpoints" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.routineEndpoints.map { refOfINFOOrCreateInfo(it) }
        ),
        "fault" to createCallSingleVararg(
            function = CodeBlock.of("listOf"),
            element.routineFaultUnion.map { refOfINFOOrCreateInfo(it) }
        ),
        "input" to refOfINFOOrCreateInfo(element.routineInput),
        "output" to refOfINFOOrCreateInfo(element.routineOutput),
    )
}
