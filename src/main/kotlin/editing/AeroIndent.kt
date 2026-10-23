package com.aerolang.aerolangsupport.editing

import com.aerolang.aerolangsupport.highlighting.AeroLexer
import com.aerolang.aerolangsupport.highlighting.AeroTokenTypes
import com.intellij.application.options.CodeStyle
import com.intellij.openapi.editor.Document
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

object AeroIndent {
    private class Opener(val type: IElementType, val lineIndent: String)

    fun unit(file: PsiFile): String {
        val options = CodeStyle.getIndentOptions(file)
        if (options.USE_TAB_CHARACTER) return "\t"
        return " ".repeat(if (options.INDENT_SIZE > 0) options.INDENT_SIZE else 4)
    }

    // End offset of the blanks that start the line
    fun blanksEnd(text: CharSequence, lineStart: Int, lineEnd: Int): Int {
        var i = lineStart
        while (i < lineEnd && (text[i] == ' ' || text[i] == '\t')) i++
        return i
    }

    // Last non-blank character before offset, or null
    fun charBefore(text: CharSequence, offset: Int): Char? {
        var i = offset - 1
        while (i >= 0 && text[i].isWhitespace()) i--
        return if (i >= 0) text[i] else null
    }

    // The indent the line starting at lineStart should have, judged from everything before it
    fun compute(doc: Document, lineStart: Int, unit: String): String {
        val text = doc.charsSequence
        val lineEnd = doc.getLineEndOffset(doc.getLineNumber(lineStart))

        val lexer = AeroLexer()
        lexer.start(text, 0, lineStart, 0)

        val stack = ArrayList<Opener>()
        var lastLineIndent = ""
        var lastWasOpener = false
        var lastType: IElementType? = null

        // Last token was `=>` or `else`: the next line is the single statement body
        var pendingBase: String? = null
        var pendingLine = -1
        // Inside such a body: after it ends the next line goes back to the base
        var bodyBase: String? = null
        var bodyLine = -1
        var bodyDepth = 0

        while (true) {
            val type = lexer.tokenType ?: break
            if (type != AeroTokenTypes.WHITE_SPACE && type != AeroTokenTypes.COMMENT) {
                val tokenLine = doc.getLineNumber(lexer.tokenStart)
                val tokenLineStart = doc.getLineStartOffset(tokenLine)
                val lineIndent = text.subSequence(tokenLineStart, blanksEnd(text, tokenLineStart, doc.getLineEndOffset(tokenLine))).toString()

                if (pendingBase != null) {
                    if (tokenLine > pendingLine) {
                        bodyBase = pendingBase
                        bodyLine = tokenLine
                        bodyDepth = stack.size
                    }
                    pendingBase = null
                } else if (bodyBase != null && tokenLine > bodyLine && stack.size == bodyDepth) {
                    bodyBase = null
                }

                lastWasOpener = false
                when (type) {
                    AeroTokenTypes.LBRACE, AeroTokenTypes.LPAREN, AeroTokenTypes.LBRACKET -> {
                        stack.add(Opener(type, lineIndent))
                        lastWasOpener = true
                    }
                    AeroTokenTypes.RBRACE, AeroTokenTypes.RPAREN, AeroTokenTypes.RBRACKET ->
                        if (stack.isNotEmpty()) stack.removeAt(stack.size - 1)
                }
                if (bodyBase != null && stack.size < bodyDepth) bodyBase = null

                val tokenText = text.subSequence(lexer.tokenStart, lexer.tokenEnd)
                if ((type == AeroTokenTypes.OPERATOR && tokenText == "=>") || (type == AeroTokenTypes.KEYWORD && tokenText == "else")) {
                    pendingBase = lineIndent
                    pendingLine = tokenLine
                }

                lastType = type
                lastLineIndent = lineIndent
            }
            lexer.advance()
        }

        // A line that starts with a closer lines up with whatever opened it
        val first = blanksEnd(text, lineStart, lineEnd)
        if (first < lineEnd && text[first] in "})]" && stack.isNotEmpty()) return stack.last().lineIndent

        pendingBase?.let { return it + unit }
        val continues = lastType == AeroTokenTypes.COMMA || lastType == AeroTokenTypes.OPERATOR
        bodyBase?.let { if (!continues && !lastWasOpener && stack.size == bodyDepth) return it }

        val top = stack.lastOrNull() ?: return lastLineIndent
        return when {
            top.type == AeroTokenTypes.LBRACE -> top.lineIndent + unit
            lastWasOpener -> top.lineIndent + unit
            else -> lastLineIndent
        }
    }
}
