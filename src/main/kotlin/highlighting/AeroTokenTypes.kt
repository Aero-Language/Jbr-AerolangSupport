package com.aerolang.aerolangsupport.highlighting

import com.intellij.psi.TokenType

object AeroTokenTypes {
    @JvmField val WHITE_SPACE = TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER = TokenType.BAD_CHARACTER

    @JvmField val TYPE = AeroTokenType("TYPE")
    @JvmField val IDENTIFIER = AeroTokenType("IDENTIFIER")
    @JvmField val ANNOTATION = AeroTokenType("ANNOTATION")
    @JvmField val FUNCTION = AeroTokenType("FUNCTION")
    @JvmField val KEYWORD = AeroTokenType("KEYWORD")

    @JvmField val INTEGER = AeroTokenType("INTEGER")
    @JvmField val FLOAT = AeroTokenType("FLOAT")
    @JvmField val STRING = AeroTokenType("STRING")
    @JvmField val CHARACTER = AeroTokenType("CHARACTER")
    @JvmField val COMMENT = AeroTokenType("COMMENT")

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

    @JvmField val OPERATOR = AeroTokenType("OPERATOR")

    val KEYWORDS = hashSetOf(
        "val", "var", "const",
        "fun", "return", "yield",
        "if", "else", "while", "for", "in", "break", "continue", "match", "case",
        "class", "struct", "record", "trait", "enum", "annotation", "operator", "op",
        "extension", "extensions", "constructor", "destructor",
        "public", "internal", "protected", "private",
        "static", "weak", "partial", "unsafe",
        "virtual", "abstract", "sealed", "impl",
        "ref",
        "module", "import", "from",
        "concurrent", "spawn",
        "true", "false", "null", "self", "it",
        "get", "set", "init",
        "is", "not", "and", "or"
    )

    val BUILTIN_TYPES = hashSetOf(
        "Int", "Float", "Bool", "Byte", "Char", "String", "Void", "Range"
    )
}