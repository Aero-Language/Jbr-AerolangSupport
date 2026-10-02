package com.aerolang.aerolangsupport.highlighting

import com.aerolang.aerolangsupport.AeroIcons
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import javax.swing.Icon

class AeroColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon = AeroIcons.small

    override fun getHighlighter(): SyntaxHighlighter = AeroSyntaxHighlighter()

    override fun getDisplayName(): String = "Aero"

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = arrayOf(
        AttributesDescriptor("Types//Class", AeroSemanticColors.CLASS),
        AttributesDescriptor("Types//Struct", AeroSemanticColors.STRUCT),
        AttributesDescriptor("Types//Record", AeroSemanticColors.RECORD),
        AttributesDescriptor("Types//Trait", AeroSemanticColors.TRAIT),
        AttributesDescriptor("Types//Enum", AeroSemanticColors.ENUM),
        AttributesDescriptor("Types//Enum member", AeroSemanticColors.ENUM_MEMBER),
        AttributesDescriptor("Types//Type parameter", AeroSemanticColors.TYPE_PARAMETER),
        AttributesDescriptor("Variables//Property", AeroSemanticColors.PROPERTY),
        AttributesDescriptor("Variables//Field", AeroSemanticColors.FIELD),
        AttributesDescriptor("Variables//Constant", AeroSemanticColors.CONSTANT),
        AttributesDescriptor("Variables//Local variable", AeroSemanticColors.LOCAL),
        AttributesDescriptor("Variables//Parameter", AeroSemanticColors.PARAMETER),
        AttributesDescriptor("Syntax//Keyword", AeroSyntaxHighlighter.KEYWORD),
        AttributesDescriptor("Syntax//Built-in type", AeroSyntaxHighlighter.TYPE),
        AttributesDescriptor("Syntax//Function", AeroSyntaxHighlighter.FUNCTION),
        AttributesDescriptor("Syntax//Annotation", AeroSyntaxHighlighter.ANNOTATION),
        AttributesDescriptor("Syntax//String", AeroSyntaxHighlighter.STRING),
        AttributesDescriptor("Syntax//Character", AeroSyntaxHighlighter.CHARACTER),
        AttributesDescriptor("Syntax//Number", AeroSyntaxHighlighter.NUMBER),
        AttributesDescriptor("Syntax//Comment", AeroSyntaxHighlighter.COMMENT),
        AttributesDescriptor("Syntax//Operator", AeroSyntaxHighlighter.OPERATOR)
    )

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey> = mapOf(
        "class" to AeroSemanticColors.CLASS,
        "struct" to AeroSemanticColors.STRUCT,
        "record" to AeroSemanticColors.RECORD,
        "trait" to AeroSemanticColors.TRAIT,
        "enum" to AeroSemanticColors.ENUM,
        "member" to AeroSemanticColors.ENUM_MEMBER,
        "tparam" to AeroSemanticColors.TYPE_PARAMETER,
        "prop" to AeroSemanticColors.PROPERTY,
        "field" to AeroSemanticColors.FIELD,
        "const" to AeroSemanticColors.CONSTANT,
        "local" to AeroSemanticColors.LOCAL,
        "param" to AeroSemanticColors.PARAMETER
    )

    override fun getDemoText(): String = """
        module Demo

        public trait <trait>Shape</trait> {
            fun Area() -> Float
        }

        public struct <struct>Point</struct>(var <field>X</field>: Int, var <field>Y</field>: Int)

        public record <record>Pair</record><<tparam>A</tparam>, <tparam>B</tparam>> {
            public val <field>First</field>: <tparam>A</tparam>
        }

        public enum <enum>Color</enum> { <member>Red</member>, <member>Green</member> }

        public class <class>Circle</class>(val <field>_r</field>: Float) : <trait>Shape</trait> {
            const <const>PI</const> = 3.14
            public <prop>Radius</prop>: Float { get => <field>_r</field> }

            public impl fun Area() -> Float {
                val <local>r</local>: Float = <field>_r</field>
                return <const>PI</const> * <local>r</local> * <local>r</local>
            }

            public fun Scale(<param>factor</param>: Float) -> <class>Circle</class> {
                <class>Circle</class>(<field>_r</field> * <param>factor</param>)
            }
        }
    """.trimIndent()
}
