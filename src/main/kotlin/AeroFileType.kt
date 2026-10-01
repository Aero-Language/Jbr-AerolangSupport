package com.aerolang.aerolangsupport

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class AeroFileType private constructor() : LanguageFileType(AeroLanguage.Instance) {
    override fun getName(): String = "Aero File"
    override fun getDescription(): String = "Aero is a compiled and strictly typed programming language, built for performance and ease of use."
    override fun getDefaultExtension(): String = "aero"
    override fun getIcon(): Icon = AeroIcons.small

    companion object {
        // @JvmField so plugin.xml's fieldName="Instance" finds a public static field
        @JvmField
        val Instance: AeroFileType = AeroFileType()
    }
}