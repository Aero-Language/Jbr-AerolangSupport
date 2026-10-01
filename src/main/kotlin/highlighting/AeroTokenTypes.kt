package com.aerolang.aerolangsupport.highlighting

import com.intellij.psi.TokenType

object AeroTokenTypes {

    // ===== Special =====

    @JvmField val WHITE_SPACE = TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER = TokenType.BAD_CHARACTER

    // ===== Identifiers =====

    @JvmField val TYPE = AeroTokenType("TYPE")
    @JvmField val IDENTIFIER = AeroTokenType("IDENTIFIER")
    @JvmField val ANNOTATION = AeroTokenType("ANNOTATION")
    @JvmField val FUNCTION = AeroTokenType("FUNCTION")
    @JvmField val KEYWORD = AeroTokenType("KEYWORD")

    // ===== Literals =====

    @JvmField val INTEGER = AeroTokenType("INTEGER")
    @JvmField val FLOAT = AeroTokenType("FLOAT")
    @JvmField val STRING = AeroTokenType("STRING")
    @JvmField val CHARACTER = AeroTokenType("CHARACTER")
    @JvmField val COMMENT = AeroTokenType("COMMENT")

    // ===== Punctuation =====

    @JvmField val DOT = AeroTokenType("DOT")
    @JvmField val COMMA = AeroTokenType("COMMA")
    @JvmField val COLON = AeroTokenType("COLON")
    @JvmField val SEMICOLON = AeroTokenType("SEMICOLON")

    @JvmField val LPAREN = AeroTokenType("LPAREN")
    @JvmField val RPAREN = AeroTokenType("RPAREN")

    @JvmField val LBRACE = AeroTokenType("LBRACE")
    @JvmField val RBRACE = AeroTokenType("RBRACE")

    @JvmField val LBRACKET = AeroTokenType("LBRACKET")
    @JvmField val RBRACKET = AeroTokenType("RBRACKET")

    // ===== Operators =====

    @JvmField val PLUS = AeroTokenType("PLUS")
    @JvmField val MINUS = AeroTokenType("MINUS")
    @JvmField val STAR = AeroTokenType("STAR")
    @JvmField val SLASH = AeroTokenType("SLASH")
    @JvmField val PERCENT = AeroTokenType("PERCENT")

    @JvmField val ASSIGN = AeroTokenType("ASSIGN")

    @JvmField val EQ = AeroTokenType("EQ")
    @JvmField val NEQ = AeroTokenType("NEQ")

    @JvmField val LT = AeroTokenType("LT")
    @JvmField val LTE = AeroTokenType("LTE")

    @JvmField val GT = AeroTokenType("GT")
    @JvmField val GTE = AeroTokenType("GTE")

    @JvmField val NOT = AeroTokenType("NOT")

    @JvmField val AND = AeroTokenType("AND")
    @JvmField val OR = AeroTokenType("OR")

    @JvmField val PLUS_ASSIGN = AeroTokenType("PLUS_ASSIGN")
    @JvmField val MINUS_ASSIGN = AeroTokenType("MINUS_ASSIGN")
    @JvmField val STAR_ASSIGN = AeroTokenType("STAR_ASSIGN")
    @JvmField val SLASH_ASSIGN = AeroTokenType("SLASH_ASSIGN")
    @JvmField val PERCENT_ASSIGN = AeroTokenType("PERCENT_ASSIGN")

    @JvmField val ARROW = AeroTokenType("ARROW")
    @JvmField val DOUBLE_COLON = AeroTokenType("DOUBLE_COLON")

    // ===== Keywords =====

    val KEYWORDS = hashSetOf(

        // Variables
        "val",
        "var",
        "const",

        // Functions
        "fun",
        "return",

        // Flow
        "if",
        "else",
        "while",
        "for",
        "in",
        "break",
        "continue",
        "match",
        "case",

        // Types
        "class",
        "struct",
        "record",
        "trait",
        "enum",
        "operator",
        "extension",

        // Modifiers
        "public",
        "internal",
        "protected",
        "private",
        "static",
        "weak",

        // Memory
        "ref",

        // Modules
        "module",
        "import",
        "from",

        // Concurrency
        "concurrent",
        "spawn",

        // Literals
        "true",
        "false",
        "null",
        "self",
        "it",

        // Properties
        "get",
        "set"
    )
}
