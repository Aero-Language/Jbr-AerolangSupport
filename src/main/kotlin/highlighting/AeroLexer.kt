package com.aerolang.aerolangsupport.highlighting

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

class AeroLexer : LexerBase() {

    private var buffer: CharSequence = ""
    private var endOffset = 0
    private var currentOffset = 0
    private var tokenStart = 0
    private var tokenEnd = 0
    private var tokenType: IElementType? = null

    // 0 normal, 1 inside the text of a $"..." string, 2 inside a {expression} of one
    private var mode = 0
    private var depth = 0

    // Longest first so "<<=" wins over "<<" and "<"
    private val operators = listOf(
        "<<=", ">>=",
        "==", "!=", "<=", ">=", "&&", "||", "^^", "++", "--", "+=", "-=", "*=", "/=", "%=",
        "&=", "|=", "^=", "->", "=>", "<<", ">>", "::", "..",
        "+", "-", "*", "/", "%", "=", "<", ">", "!", "&", "|", "^", "~", "?"
    )

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.endOffset = endOffset
        currentOffset = startOffset
        mode = if (initialState == 0) 0 else if (initialState == 1) 1 else 2
        depth = if (initialState >= 2) initialState - 2 else 0
        advance()
    }

    override fun getState() = when (mode) { 0 -> 0; 1 -> 1; else -> 2 + depth }
    override fun getTokenType() = tokenType
    override fun getTokenStart() = tokenStart
    override fun getTokenEnd() = tokenEnd
    override fun getBufferSequence() = buffer
    override fun getBufferEnd() = endOffset

    private fun peek(offset: Int = 0): Char? {
        val index = currentOffset + offset
        return if (index >= endOffset) null else buffer[index]
    }

    private fun next() {
        currentOffset++
    }

    private fun match(text: String): Boolean {
        if (currentOffset + text.length > endOffset) return false
        for (i in text.indices) if (buffer[currentOffset + i] != text[i]) return false
        currentOffset += text.length
        return true
    }

    private fun single(type: IElementType) {
        next()
        tokenType = type
    }

    private fun readWhitespace() {
        while (peek()?.isWhitespace() == true) next()
        tokenType = AeroTokenTypes.WHITE_SPACE
    }

    private fun readComment(): Boolean {
        if (match("//")) {
            while (peek() != null && peek() != '\n') next()
            tokenType = AeroTokenTypes.COMMENT
            return true
        }
        if (match("/*")) {
            while (peek() != null && !match("*/")) next()
            tokenType = AeroTokenTypes.COMMENT
            return true
        }
        return false
    }

    private fun readString(quote: Char) {
        if (quote == '"' && match("\"\"\"")) {
            while (peek() != null && !match("\"\"\"")) next()
            tokenType = AeroTokenTypes.STRING
            return
        }

        next() // opening quote
        while (true) {
            val c = peek() ?: break
            if (c == '\n') break // unterminated, don't swallow the rest of the file
            if (c == '\\') {
                next()
                if (peek() != null && peek() != '\n') next()
                continue
            }
            next()
            if (c == quote) break
        }
        tokenType = if (quote == '\'') AeroTokenTypes.CHARACTER else AeroTokenTypes.STRING
    }

    private fun readNumber() {
        val p1 = peek(1)
        if (peek() == '0' && (p1 == 'x' || p1 == 'X')) {
            next(); next()
            while (peek()?.let { it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == '_' } == true) next()
            tokenType = AeroTokenTypes.INTEGER
            return
        }
        if (peek() == '0' && (p1 == 'b' || p1 == 'B')) {
            next(); next()
            while (peek() == '0' || peek() == '1' || peek() == '_') next()
            tokenType = AeroTokenTypes.INTEGER
            return
        }

        while (peek()?.isDigit() == true || peek() == '_') next()

        var isFloat = false
        if (peek() == '.' && peek(1)?.isDigit() == true) {
            isFloat = true
            next()
            while (peek()?.isDigit() == true || peek() == '_') next()
        }
        tokenType = if (isFloat) AeroTokenTypes.FLOAT else AeroTokenTypes.INTEGER
    }

    private fun readInterpolatedText(c: Char) {
        when (c) {
            '"' -> { next(); mode = 0; tokenType = AeroTokenTypes.STRING }
            '{' -> { next(); mode = 2; depth = 0; tokenType = AeroTokenTypes.LBRACE }
            else -> {
                while (true) {
                    val ch = peek() ?: break
                    if (ch == '"' || ch == '{' || ch == '\n') break
                    next()
                    if (ch == '\\' && peek() != null && peek() != '\n') next()
                }
                tokenType = AeroTokenTypes.STRING
            }
        }
    }

    private fun readAnnotation() {
        next() // '@'
        val c = peek()
        if (c == null || (!c.isLetter() && c != '_')) {
            tokenType = AeroTokenTypes.BAD_CHARACTER
            return
        }
        while (peek()?.let { it.isLetterOrDigit() || it == '_' } == true) next()
        tokenType = AeroTokenTypes.ANNOTATION
    }

    private fun readIdentifier() {
        while (peek()?.let { it.isLetterOrDigit() || it == '_' } == true) next()

        val text = buffer.subSequence(tokenStart, currentOffset).toString()
        if (text in AeroTokenTypes.KEYWORDS) {
            tokenType = AeroTokenTypes.KEYWORD
            return
        }
        if (text in AeroTokenTypes.BUILTIN_TYPES) {
            tokenType = AeroTokenTypes.TYPE
            return
        }

        // Followed by '(' (ignoring whitespace) means a call or declaration name
        var look = currentOffset
        while (look < endOffset && buffer[look].isWhitespace()) look++
        tokenType = if (look < endOffset && buffer[look] == '(') AeroTokenTypes.FUNCTION else AeroTokenTypes.IDENTIFIER
    }

    private fun readOperator(): Boolean {
        for (op in operators) {
            if (match(op)) {
                tokenType = AeroTokenTypes.OPERATOR
                return true
            }
        }
        return false
    }

    override fun advance() {
        if (currentOffset >= endOffset) {
            tokenType = null
            tokenStart = endOffset
            tokenEnd = endOffset
            return
        }

        tokenStart = currentOffset
        val c = peek()!!

        if (mode == 1) {
            if (c == '\n') mode = 0 // unterminated, fall back to normal lexing
            else {
                readInterpolatedText(c)
                tokenEnd = currentOffset
                return
            }
        }

        when {
            c.isWhitespace() -> {
                readWhitespace()
                if (mode != 0 && buffer.subSequence(tokenStart, currentOffset).contains('\n')) mode = 0
            }
            c == '/' -> if (!readComment() && !readOperator()) single(AeroTokenTypes.BAD_CHARACTER)
            c == '"' || c == '\'' -> readString(c)
            c == '$' && peek(1) == '"' -> {
                next(); next()
                mode = 1
                tokenType = AeroTokenTypes.STRING
            }
            c.isDigit() -> readNumber()
            c.isLetter() || c == '_' -> readIdentifier()
            c == '@' -> readAnnotation()
            c == '.' && peek(1) == '.' -> readOperator()
            else -> when (c) {
                '(' -> single(AeroTokenTypes.LPAREN)
                ')' -> single(AeroTokenTypes.RPAREN)
                '{' -> {
                    if (mode == 2) depth++
                    single(AeroTokenTypes.LBRACE)
                }
                '}' -> {
                    if (mode == 2) { if (depth == 0) mode = 1 else depth-- }
                    single(AeroTokenTypes.RBRACE)
                }
                '[' -> single(AeroTokenTypes.LBRACKET)
                ']' -> single(AeroTokenTypes.RBRACKET)
                '.' -> single(AeroTokenTypes.DOT)
                ',' -> single(AeroTokenTypes.COMMA)
                ';' -> single(AeroTokenTypes.SEMICOLON)
                ':' -> if (!readOperator()) single(AeroTokenTypes.COLON) // "::" is an operator
                else -> if (!readOperator()) single(AeroTokenTypes.BAD_CHARACTER)
            }
        }

        tokenEnd = currentOffset
    }
}