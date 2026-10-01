package com.aerolang.aerolangsupport.highlighting

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class AeroLexer : LexerBase() {

    private lateinit var buffer: CharSequence

    private var startOffset = 0
    private var endOffset = 0

    private var currentOffset = 0

    private var tokenStart = 0
    private var tokenEnd = 0

    private var tokenType: IElementType? = null

    override fun start(
        buffer: CharSequence,
        startOffset: Int,
        endOffset: Int,
        initialState: Int
    ) {
        this.buffer = buffer
        this.startOffset = startOffset
        this.endOffset = endOffset

        currentOffset = startOffset

        advance()
    }

    override fun getState() = 0

    override fun getTokenType() = tokenType

    override fun getTokenStart() = tokenStart

    override fun getTokenEnd() = tokenEnd

    override fun getBufferSequence() = buffer

    override fun getBufferEnd() = endOffset

    // ------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------

    private fun peek(offset: Int = 0): Char? {
        val index = currentOffset + offset
        return if (index >= endOffset) null else buffer[index]
    }

    private fun advanceChar() {
        currentOffset++
    }

    private fun match(text: String): Boolean {

        if (currentOffset + text.length > endOffset)
            return false

        for (i in text.indices) {
            if (buffer[currentOffset + i] != text[i])
                return false
        }

        currentOffset += text.length
        return true
    }

    private fun skipWhitespace() {

        while (peek()?.isWhitespace() == true)
            advanceChar()

        tokenType = AeroTokenTypes.WHITE_SPACE
    }

    private fun single(type: IElementType) {
        advanceChar()
        tokenType = type
    }

    private fun operator(
        twoChar: String,
        twoToken: IElementType,
        oneToken: IElementType
    ) {
        if (match(twoChar))
            tokenType = twoToken
        else
            single(oneToken)
    }

    // ------------------------------------------------------------
    // Comments
    // ------------------------------------------------------------

    private fun readSlash() {

        if (match("//")) {

            while (peek() != null && peek() != '\n')
                advanceChar()

            tokenType = AeroTokenTypes.COMMENT
            return
        }

        if (match("/*")) {

            while (peek() != null) {

                if (match("*/"))
                    break

                advanceChar()
            }

            tokenType = AeroTokenTypes.COMMENT
            return
        }

        if (match("/=")) {
            tokenType = AeroTokenTypes.SLASH_ASSIGN
            return
        }

        advanceChar()
        tokenType = AeroTokenTypes.SLASH
    }


    // ------------------------------------------------------------
    // Strings
    // ------------------------------------------------------------

    private fun readString(quote: Char) {

        advanceChar()

        while (peek() != null) {

            val c = peek()!!

            if (c == '\\') {
                advanceChar()
                if (peek() != null)
                    advanceChar()
                continue
            }

            if (c == quote) {
                advanceChar()
                break
            }

            advanceChar()
        }

        tokenType = AeroTokenTypes.STRING
    }

    // ------------------------------------------------------------
    // Numbers
    // ------------------------------------------------------------

    private fun readNumber() {

        while (peek()?.isDigit() == true || peek() == '_')
            advanceChar()

        var isFloat = false

        if (peek() == '.' && peek(1)?.isDigit() == true) {

            isFloat = true

            advanceChar()

            while (peek()?.isDigit() == true || peek() == '_')
                advanceChar()
        }

        tokenType =
            if (isFloat)
                AeroTokenTypes.FLOAT
            else
                AeroTokenTypes.INTEGER
    }

    // ------------------------------------------------------------
    // Identifiers
    // ------------------------------------------------------------

    private fun readAnnotation() {

        advanceChar() // consume '@'

        if (peek() == null || (!peek()!!.isLetter() && peek() != '_')) {
            tokenType = AeroTokenTypes.BAD_CHARACTER
            return
        }

        while (peek() != null &&
            (peek()!!.isLetterOrDigit() || peek() == '_')) {
            advanceChar()
        }

        tokenType = AeroTokenTypes.ANNOTATION
    }

    private fun readIdentifier() {

        while (true) {

            val c = peek() ?: break

            if (!c.isLetterOrDigit() && c != '_')
                break

            advanceChar()
        }

        val text = buffer.subSequence(tokenStart, currentOffset).toString()

        if (AeroTokenTypes.KEYWORDS.contains(text)) {

            tokenType = AeroTokenTypes.KEYWORD
            return
        }

        var lookahead = currentOffset

        while (
            lookahead < endOffset &&
            buffer[lookahead].isWhitespace()
        ) {
            lookahead++
        }

        tokenType =
            if (lookahead < endOffset && buffer[lookahead] == '(')
                AeroTokenTypes.FUNCTION
            else
                AeroTokenTypes.IDENTIFIER
    }

    // ------------------------------------------------------------
    // Lexer
    // ------------------------------------------------------------

    override fun advance() {

        if (currentOffset >= endOffset) {

            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset

            return
        }

        tokenStart = currentOffset

        when (val c = peek()!!) {

            // whitespace

            ' ', '\t', '\n', '\r' ->
                skipWhitespace()

            // comments

            '/' ->
                readSlash()

            // strings

            '"' ->
                readString('"')

            '\'' ->
                readString('\'')

            // numbers

            in '0'..'9' ->
                readNumber()

            // identifiers

            in 'a'..'z',
            in 'A'..'Z',
            '_' -> readIdentifier()
            '@' -> readAnnotation()

            // punctuation

            '(' -> single(AeroTokenTypes.LPAREN)
            ')' -> single(AeroTokenTypes.RPAREN)
            '{' -> single(AeroTokenTypes.LBRACE)
            '}' -> single(AeroTokenTypes.RBRACE)
            '[' -> single(AeroTokenTypes.LBRACKET)
            ']' -> single(AeroTokenTypes.RBRACKET)
            '.' -> single(AeroTokenTypes.DOT)
            ',' -> single(AeroTokenTypes.COMMA)
            ';' -> single(AeroTokenTypes.SEMICOLON)
            ':' -> operator("::", AeroTokenTypes.DOUBLE_COLON, AeroTokenTypes.COLON)
            '+' -> operator("+=", AeroTokenTypes.PLUS_ASSIGN, AeroTokenTypes.PLUS)
            '*' -> operator("*=", AeroTokenTypes.STAR_ASSIGN, AeroTokenTypes.STAR)
            '%' -> operator("%=", AeroTokenTypes.PERCENT_ASSIGN, AeroTokenTypes.PERCENT)
            '=' -> operator("==", AeroTokenTypes.EQ, AeroTokenTypes.ASSIGN)
            '!' -> operator("!=", AeroTokenTypes.NEQ, AeroTokenTypes.NOT)
            '<' -> operator("<=", AeroTokenTypes.LTE, AeroTokenTypes.LT)
            '>' -> operator(">=", AeroTokenTypes.GTE, AeroTokenTypes.GT)

            '-' -> {
                if (match("->")) tokenType = AeroTokenTypes.ARROW
                else if (match("-=")) tokenType = AeroTokenTypes.MINUS_ASSIGN
                else {
                    advanceChar()
                    tokenType = AeroTokenTypes.MINUS
                }
            }

            else -> {
                advanceChar()
                tokenType = AeroTokenTypes.BAD_CHARACTER
            }
        }

        tokenEnd = currentOffset
    }
}
