package org.cufy.mmrpc.compact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cufy.mmrpc.*

@Serializable
@SerialName("union")
data class CompactUnionDefinition(
    override val name: String = UnionDefinition.ANONYMOUS_NAME,
    override val namespace: Namespace = Namespace.Toplevel,
    @SerialName("is_inline")
    override val isInline: Boolean = true,
    override val description: String = "",
    override val metadata: List<CompactMetadataDefinitionUsage> = emptyList(),
    @SerialName("union_discriminator")
    val unionDiscriminator: String,
    @SerialName("union_types.ref")
    val unionTypes: List<CanonicalName>,
) : CompactElementDefinition

fun UnionDefinition.toCompact(): CompactUnionDefinition {
    return CompactUnionDefinition(
        name = this.name,
        namespace = this.namespace,
        isInline = this.isInline,
        description = this.description,
        metadata = this.metadata
            .map { it.toCompact() },
        unionDiscriminator = this.unionDiscriminator,
        unionTypes = this.unionTypes
            .map { it.canonicalName }
    )
}

fun CompactUnionDefinition.inflate(
    onLookup: (CanonicalName) -> ElementDefinition?
): () -> UnionDefinition? {
    return it@{
        UnionDefinition(
            name = this.name,
            namespace = this.namespace,
            isInline = this.isInline,
            description = this.description,
            metadata = this.metadata.map {
                it.inflate(onLookup)() ?: return@it null
            },
            unionDiscriminator = this.unionDiscriminator,
            unionTypes = this.unionTypes.map {
                val item = onLookup(it) ?: return@it null
                require(item is StructDefinition) {
                    "union_types.ref must point to a StructDefinition"
                }
                item
            },
        )
    }
}
