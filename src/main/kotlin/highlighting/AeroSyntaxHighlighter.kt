package com.aerolang.aerolangsupport.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType

class AeroSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = AeroLexer()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> =
        when (tokenType) {

            // Keywords

            AeroTokenTypes.KEYWORD ->
                KEYWORD_KEYS

            // Types

            AeroTokenTypes.TYPE ->
                TYPE_KEYS

            // Functions

            AeroTokenTypes.FUNCTION ->
                FUNCTION_KEYS

            // Annotations

            AeroTokenTypes.ANNOTATION ->
                ANNOTATION_KEYS

            // Literals

            AeroTokenTypes.INTEGER,
            AeroTokenTypes.FLOAT ->
                NUMBER_KEYS

            AeroTokenTypes.STRING ->
                STRING_KEYS

            AeroTokenTypes.CHARACTER ->
                CHARACTER_KEYS

            // Comments

            AeroTokenTypes.COMMENT ->
                COMMENT_KEYS

            // Punctuation

            AeroTokenTypes.DOT,
            AeroTokenTypes.COMMA,
            AeroTokenTypes.COLON,
            AeroTokenTypes.SEMICOLON,
            AeroTokenTypes.LPAREN,
            AeroTokenTypes.RPAREN,
            AeroTokenTypes.LBRACE,
            AeroTokenTypes.RBRACE,
            AeroTokenTypes.LBRACKET,
            AeroTokenTypes.RBRACKET ->
                PUNCTUATION_KEYS

            // Operators

            AeroTokenTypes.PLUS,
            AeroTokenTypes.MINUS,
            AeroTokenTypes.STAR,
            AeroTokenTypes.SLASH,
            AeroTokenTypes.PERCENT,
            AeroTokenTypes.ASSIGN,
            AeroTokenTypes.EQ,
            AeroTokenTypes.NEQ,
            AeroTokenTypes.LT,
            AeroTokenTypes.LTE,
            AeroTokenTypes.GT,
            AeroTokenTypes.GTE,
            AeroTokenTypes.NOT,
            AeroTokenTypes.AND,
            AeroTokenTypes.OR,
            AeroTokenTypes.PLUS_ASSIGN,
            AeroTokenTypes.MINUS_ASSIGN,
            AeroTokenTypes.STAR_ASSIGN,
            AeroTokenTypes.SLASH_ASSIGN,
            AeroTokenTypes.PERCENT_ASSIGN,
            AeroTokenTypes.ARROW,
            AeroTokenTypes.DOUBLE_COLON ->
                OPERATOR_KEYS

            AeroTokenTypes.BAD_CHARACTER ->
                BAD_CHARACTER_KEYS

            else ->
                EMPTY_KEYS
        }

    companion object {

        // --------------------------------------------------------------------
        // Language Elements
        // --------------------------------------------------------------------

        val KEYWORD = TextAttributesKey.createTextAttributesKey(
            "AERO_KEYWORD",
            DefaultLanguageHighlighterColors.KEYWORD
        )

        val TYPE = TextAttributesKey.createTextAttributesKey(
            "AERO_TYPE",
            DefaultLanguageHighlighterColors.CLASS_NAME
        )

        val FUNCTION = TextAttributesKey.createTextAttributesKey(
            "AERO_FUNCTION",
            DefaultLanguageHighlighterColors.FUNCTION_CALL
        )

        val ANNOTATION = TextAttributesKey.createTextAttributesKey(
            "AERO_ANNOTATION",
            DefaultLanguageHighlighterColors.METADATA
        )

        // --------------------------------------------------------------------
        // Literals
        // --------------------------------------------------------------------

        val STRING = TextAttributesKey.createTextAttributesKey(
            "AERO_STRING",
            DefaultLanguageHighlighterColors.STRING
        )

        val CHARACTER = TextAttributesKey.createTextAttributesKey(
            "AERO_CHARACTER",
            DefaultLanguageHighlighterColors.STRING
        )

        val NUMBER = TextAttributesKey.createTextAttributesKey(
            "AERO_NUMBER",
            DefaultLanguageHighlighterColors.NUMBER
        )

        // --------------------------------------------------------------------
        // Comments
        // --------------------------------------------------------------------

        val COMMENT = TextAttributesKey.createTextAttributesKey(
            "AERO_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )

        // --------------------------------------------------------------------
        // Symbols
        // --------------------------------------------------------------------

        val OPERATOR = TextAttributesKey.createTextAttributesKey(
            "AERO_OPERATOR",
            DefaultLanguageHighlighterColors.OPERATION_SIGN
        )

        val PUNCTUATION = TextAttributesKey.createTextAttributesKey(
            "AERO_PUNCTUATION",
            DefaultLanguageHighlighterColors.DOT
        )

        // --------------------------------------------------------------------
        // Errors
        // --------------------------------------------------------------------

        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey(
            "AERO_BAD_CHARACTER",
            DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE
        )

        // --------------------------------------------------------------------
        // Key Arrays
        // --------------------------------------------------------------------

        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val TYPE_KEYS = arrayOf(TYPE)
        private val FUNCTION_KEYS = arrayOf(FUNCTION)
        private val ANNOTATION_KEYS = arrayOf(ANNOTATION)

        private val STRING_KEYS = arrayOf(STRING)
        private val CHARACTER_KEYS = arrayOf(CHARACTER)
        private val NUMBER_KEYS = arrayOf(NUMBER)

        private val COMMENT_KEYS = arrayOf(COMMENT)

        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val PUNCTUATION_KEYS = arrayOf(PUNCTUATION)

        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)

        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}
