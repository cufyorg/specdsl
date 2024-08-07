package org.cufy.mmrpc.gen.kotlin.util.gen.references

import org.cufy.mmrpc.ElementDefinition
import org.cufy.mmrpc.Marker3
import org.cufy.mmrpc.gen.kotlin.GenGroup
import org.cufy.mmrpc.gen.kotlin.GenPackaging
import org.cufy.mmrpc.gen.kotlin.util.furtherEscape
import org.cufy.mmrpc.gen.kotlin.util.gen.debugRequireGeneratedClass

private const val TAG = "generatePackageOf"

@Marker3
fun GenGroup.generatedPackageOf(element: ElementDefinition): String {
    debugRequireGeneratedClass(TAG, element)

    return when (ctx.packaging) {
        GenPackaging.SUB_PACKAGES -> implSubPackages(element)
    }
}

private fun GenGroup.implSubPackages(element: ElementDefinition): String {
    val ns = rootNSOf(element)
    val nsv = ns.canonicalName.value.furtherEscape()
    return when {
        ctx.pkg.isEmpty() -> nsv
        else -> "${ctx.pkg}.$nsv"
    }
}
