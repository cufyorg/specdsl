package org.cufy.specdsl.compact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cufy.specdsl.CanonicalName
import org.cufy.specdsl.ElementDefinition
import org.cufy.specdsl.Namespace
import org.cufy.specdsl.ScalarDefinition

@Serializable
@SerialName("scalar")
data class CompactScalarDefinition(
    override val name: String = ScalarDefinition.ANONYMOUS_NAME,
    override val namespace: Namespace = Namespace.Toplevel,
    @SerialName("is_inline")
    override val isInline: Boolean = true,
    override val description: String = "",
    override val metadata: List<CompactMetadata> = emptyList(),
) : CompactElementDefinition

fun ScalarDefinition.toCompact(): CompactScalarDefinition {
    return CompactScalarDefinition(
        name = this.name,
        namespace = this.namespace,
        isInline = this.isInline,
        description = this.description,
        metadata = this.metadata
            .map { it.toCompact() },
    )
}

fun CompactScalarDefinition.inflate(
    onLookup: (CanonicalName) -> ElementDefinition?
): () -> ScalarDefinition? {
    return it@{
        ScalarDefinition(
            name = this.name,
            namespace = this.namespace,
            isInline = this.isInline,
            description = this.description,
            metadata = this.metadata.map {
                it.inflate(onLookup)() ?: return@it null
            },
        )
    }
}
