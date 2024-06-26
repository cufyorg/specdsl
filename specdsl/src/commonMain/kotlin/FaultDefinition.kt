/*
 *	Copyright 2024 cufy.org
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.specdsl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.cufy.specdsl.ConstDefinition.Companion.ANONYMOUS_NAME

////////////////////////////////////////

@Serializable
@SerialName("fault")
data class FaultDefinition(
    override val name: String = ANONYMOUS_NAME,
    override val namespace: Namespace = Namespace.Toplevel,
    @SerialName("is_inline")
    override val isInline: Boolean = true,
    override val description: String = "",
    override val metadata: List<Metadata> = emptyList(),
) : ElementDefinition() {
    companion object {
        const val ANONYMOUS_NAME = "(anonymous<fault>)"
    }

    override fun collectChildren() = sequence {
        yieldAll(metadata.asSequence().flatMap { it.collect() })
    }
}

open class FaultDefinitionBuilder :
    ElementDefinitionBuilder() {
    override var name = FaultDefinition.ANONYMOUS_NAME

    override fun build(): FaultDefinition {
        val asNamespace = this.namespace.value + this.name
        return FaultDefinition(
            name = this.name,
            namespace = this.namespace.value,
            isInline = this.isInline,
            description = this.description,
            metadata = this.metadata.toList(),
        )
    }
}

@Marker1
fun fault(
    block: FaultDefinitionBuilder.() -> Unit = {}
): Unnamed<FaultDefinition> {
    return Unnamed { namespace, name ->
        FaultDefinitionBuilder()
            .also { it.name = name ?: return@also }
            .also { it.namespace *= namespace }
            .also { it.isInline = name == null }
            .apply(block)
            .build()
    }
}

////////////////////////////////////////

@Marker1
val fault = fault()

////////////////////////////////////////
