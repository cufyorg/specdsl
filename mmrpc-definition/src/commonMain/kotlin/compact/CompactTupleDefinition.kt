package org.cufy.mmrpc.compact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cufy.mmrpc.*

@Serializable
@SerialName("tuple")
data class CompactTupleDefinition(
    override val name: String = TupleDefinition.ANONYMOUS_NAME,
    override val namespace: Namespace = Namespace.Toplevel,
    @SerialName("is_inline")
    override val isInline: Boolean = true,
    override val description: String = "",
    override val metadata: List<CompactMetadataDefinitionUsage> = emptyList(),
    @SerialName("tuple_types.ref")
    val tupleTypes: List<CanonicalName> = emptyList(),
) : CompactElementDefinition

fun TupleDefinition.toCompact(): CompactTupleDefinition {
    return CompactTupleDefinition(
        name = this.name,
        namespace = this.namespace,
        isInline = this.isInline,
        description = this.description,
        metadata = this.metadata
            .map { it.toCompact() },
        tupleTypes = this.tupleTypes
            .map { it.canonicalName },
    )
}

fun CompactTupleDefinition.inflate(
    onLookup: (CanonicalName) -> ElementDefinition?
): () -> TupleDefinition? {
    return it@{
        TupleDefinition(
            name = this.name,
            namespace = this.namespace,
            isInline = this.isInline,
            description = this.description,
            metadata = this.metadata.map {
                it.inflate(onLookup)() ?: return@it null
            },
            tupleTypes = this.tupleTypes.map {
                val item = onLookup(it) ?: return@it null
                require(item is TypeDefinition) {
                    "tuple_types.ref must point to a TypeDefinition"
                }
                item
            },
        )
    }
}
