package org.cufy.mmrpc.gen.kotlin.util.gen

import org.cufy.mmrpc.*
import org.cufy.mmrpc.gen.kotlin.GenGroup

@Marker3
fun GenGroup.hasGeneratedClass(element: ElementDefinition): Boolean {
    if (element.isAnonymous) return false
    if (element is ArrayDefinition) return false
    if (element is OptionalDefinition) return false
    if (element is ScalarDefinition && isNative(element)) return false
    if (element is MetadataDefinition && isNative(element)) return false
    val parent = ctx.elementsNS[element.namespace] ?: return true
    return hasGeneratedClass(parent)
}
