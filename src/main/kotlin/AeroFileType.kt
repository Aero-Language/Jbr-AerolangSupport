package com.aerolang.aerolangsupport

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.NlsContexts
import com.intellij.openapi.util.NlsSafe
import com.intellij.ui.JBColor
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class AeroFileType : FileType {
    override fun getDefaultExtension(): @NlsSafe String { return "aero" }
    override fun getDescription(): @NlsContexts.Label String { return "Aero is a compiled and strictly typed programming language, built for performance and ease of use." }
    override fun getName(): @NonNls String { return "Aero File" }
    override fun isBinary(): Boolean { return false }
    override fun getIcon(): Icon { return AeroIcons.small }

    companion object{
        val Instance: AeroFileType = AeroFileType()
    }
}