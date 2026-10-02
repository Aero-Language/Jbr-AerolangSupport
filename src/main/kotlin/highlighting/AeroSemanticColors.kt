package com.aerolang.aerolangsupport.highlighting

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey

// The default colors live in colorSchemes/AeroLight.xml and AeroDark.xml, these fallbacks only apply to other schemes
object AeroSemanticColors {
    val CLASS = key("AERO_CLASS", DefaultLanguageHighlighterColors.CLASS_NAME)
    val STRUCT = key("AERO_STRUCT", DefaultLanguageHighlighterColors.CLASS_NAME)
    val RECORD = key("AERO_RECORD", DefaultLanguageHighlighterColors.CLASS_NAME)
    val TRAIT = key("AERO_TRAIT", DefaultLanguageHighlighterColors.INTERFACE_NAME)
    val ENUM = key("AERO_ENUM", DefaultLanguageHighlighterColors.CLASS_NAME)
    val ENUM_MEMBER = key("AERO_ENUM_MEMBER", DefaultLanguageHighlighterColors.STATIC_FIELD)
    val TYPE_PARAMETER = key("AERO_TYPE_PARAMETER", DefaultLanguageHighlighterColors.CLASS_NAME)
    val PROPERTY = key("AERO_PROPERTY", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
    val FIELD = key("AERO_FIELD", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
    val CONSTANT = key("AERO_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
    val LOCAL = key("AERO_LOCAL", DefaultLanguageHighlighterColors.LOCAL_VARIABLE)
    val PARAMETER = key("AERO_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER)

    private fun key(name: String, fallback: TextAttributesKey) = TextAttributesKey.createTextAttributesKey(name, fallback)

    // Token types are the ones the language server sends; anything else keeps the lexer's colors
    fun keyFor(tokenType: String): TextAttributesKey? = when (tokenType) {
        "class" -> CLASS
        "struct" -> STRUCT
        "record" -> RECORD
        "interface" -> TRAIT
        "enum" -> ENUM
        "enumMember" -> ENUM_MEMBER
        "typeParameter" -> TYPE_PARAMETER
        "property" -> PROPERTY
        "field" -> FIELD
        "constant" -> CONSTANT
        "variable" -> LOCAL
        "parameter" -> PARAMETER
        else -> null
    }
}
