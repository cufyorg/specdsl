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

////////////////////////////////////////

@Serializable
@SerialName("const")
data class ConstDefinition(
    override val name: String = "(anonymous<const>)",
    override val namespace: Namespace = Namespace.Toplevel,
    @SerialName("is_inline")
    override val isInline: Boolean = true,
    override val description: String = "",
    override val decorators: List<DecoratorDefinition> = emptyList(),
    @SerialName("const_value")
    val constValue: String,
) : TypeDefinition {
    override fun collectChildren() = sequence {
        yieldAll(decorators.asSequence().flatMap { it.collect() })
    }
}

open class ConstDefinitionBuilder :
    ElementDefinitionBuilder() {
    override var name = "(anonymous<const>)"

    open lateinit var value: String

    override fun build(): ConstDefinition {
        val asNamespace = this.namespace.value + this.name
        return ConstDefinition(
            name = this.name,
            namespace = this.namespace.value,
            isInline = this.isInline,
            description = this.description,
            decorators = this.decoratorsUnnamed.map {
                it.get(asNamespace)
            },
            constValue = this.value,
        )
    }
}

@Marker1
internal fun const(
    block: ConstDefinitionBuilder.() -> Unit = {}
): Unnamed<ConstDefinition> {
    return Unnamed { namespace, name ->
        ConstDefinitionBuilder()
            .also { it.name = name ?: return@also }
            .also { it.namespace *= namespace }
            .also { it.isInline = name == null }
            .apply(block)
            .build()
    }
}

////////////////////////////////////////

@Marker1
fun const(
    value: String,
    block: ConstDefinitionBuilder.() -> Unit = {}
): Unnamed<ConstDefinition> {
    return const { this.value = value; block() }
}

@Marker1
fun constString(
    value: String,
    block: ConstDefinitionBuilder.() -> Unit = {}
): Unnamed<ConstDefinition> {
    return const { this.value = "\"$value\""; block() }
}

////////////////////////////////////////