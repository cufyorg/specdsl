package org.cufy.mmrpc.gen.kotlin.util.gen

import org.cufy.mmrpc.Marker3
import org.cufy.mmrpc.ScalarDefinition
import org.cufy.mmrpc.TypeDefinition
import org.cufy.mmrpc.gen.kotlin.GenGroup

@Marker3
fun GenGroup.isCompileConst(element: TypeDefinition): Boolean {
    return element is ScalarDefinition && isNative(element)
}
