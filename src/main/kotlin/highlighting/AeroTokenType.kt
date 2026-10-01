package com.aerolang.aerolangsupport.highlighting

import com.aerolang.aerolangsupport.AeroLanguage
import com.intellij.psi.tree.IElementType

class AeroTokenType(debugName: String) : IElementType(debugName, AeroLanguage.Instance) {
    override fun toString(): String = "AeroTokenType." + super.toString()
}