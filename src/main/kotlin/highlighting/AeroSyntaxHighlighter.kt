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
            AeroTokenTypes.KEYWORD -> KEYWORD_KEYS
            AeroTokenTypes.TYPE -> TYPE_KEYS
            AeroTokenTypes.FUNCTION -> FUNCTION_KEYS
            AeroTokenTypes.ANNOTATION -> ANNOTATION_KEYS

            AeroTokenTypes.INTEGER,
            AeroTokenTypes.FLOAT -> NUMBER_KEYS

            AeroTokenTypes.STRING -> STRING_KEYS
            AeroTokenTypes.CHARACTER -> CHARACTER_KEYS
            AeroTokenTypes.COMMENT -> COMMENT_KEYS

            AeroTokenTypes.DOT -> DOT_KEYS
            AeroTokenTypes.COMMA -> COMMA_KEYS
            AeroTokenTypes.SEMICOLON -> SEMICOLON_KEYS
            AeroTokenTypes.COLON -> PUNCTUATION_KEYS

            AeroTokenTypes.LPAREN,
            AeroTokenTypes.RPAREN -> PAREN_KEYS

            AeroTokenTypes.LBRACE,
            AeroTokenTypes.RBRACE -> BRACE_KEYS

            AeroTokenTypes.LBRACKET,
            AeroTokenTypes.RBRACKET -> BRACKET_KEYS

            AeroTokenTypes.OPERATOR -> OPERATOR_KEYS
            AeroTokenTypes.BAD_CHARACTER -> BAD_CHARACTER_KEYS
            else -> EMPTY_KEYS
        }

    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey("AERO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val TYPE = TextAttributesKey.createTextAttributesKey("AERO_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME)
        val FUNCTION = TextAttributesKey.createTextAttributesKey("AERO_FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val ANNOTATION = TextAttributesKey.createTextAttributesKey("AERO_ANNOTATION", DefaultLanguageHighlighterColors.METADATA)
        val STRING = TextAttributesKey.createTextAttributesKey("AERO_STRING", DefaultLanguageHighlighterColors.STRING)
        val CHARACTER = TextAttributesKey.createTextAttributesKey("AERO_CHARACTER", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = TextAttributesKey.createTextAttributesKey("AERO_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val COMMENT = TextAttributesKey.createTextAttributesKey("AERO_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val OPERATOR = TextAttributesKey.createTextAttributesKey("AERO_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PUNCTUATION = TextAttributesKey.createTextAttributesKey("AERO_PUNCTUATION", DefaultLanguageHighlighterColors.DOT)
        val DOT = TextAttributesKey.createTextAttributesKey("AERO_DOT", DefaultLanguageHighlighterColors.DOT)
        val COMMA = TextAttributesKey.createTextAttributesKey("AERO_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val SEMICOLON = TextAttributesKey.createTextAttributesKey("AERO_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val PAREN = TextAttributesKey.createTextAttributesKey("AERO_PAREN", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACE = TextAttributesKey.createTextAttributesKey("AERO_BRACE", DefaultLanguageHighlighterColors.BRACES)
        val BRACKET = TextAttributesKey.createTextAttributesKey("AERO_BRACKET", DefaultLanguageHighlighterColors.BRACKETS)
        val BAD_CHARACTER = TextAttributesKey.createTextAttributesKey("AERO_BAD_CHARACTER", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE)

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
        private val DOT_KEYS = arrayOf(DOT)
        private val COMMA_KEYS = arrayOf(COMMA)
        private val SEMICOLON_KEYS = arrayOf(SEMICOLON)
        private val PAREN_KEYS = arrayOf(PAREN)
        private val BRACE_KEYS = arrayOf(BRACE)
        private val BRACKET_KEYS = arrayOf(BRACKET)
        private val BAD_CHARACTER_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}